package com.example.ConstructionCompanyApplication.service

import com.example.ConstructionCompanyApplication.APIConfiguration
import com.example.ConstructionCompanyApplication.APIConfiguration.Companion.API_BASE_URL
import com.example.ConstructionCompanyApplication.dto.*
import com.example.ConstructionCompanyApplication.repository.*
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.SimpleType
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule
import org.springframework.stereotype.Service
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.IOException
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation


abstract class AbstractService<R : AbstractRepository, E : AbstractDto>(private val repositoryClass: KClass<R>) :
    APIConfiguration {

    private val dtoType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
    private val objectMapper = ObjectMapper().registerModule(Jackson2HalModule())
    private val typeFactory = objectMapper.typeFactory
    private val repository: R = run {
        val repositoryUrl = repositoryClass.findAnnotation<BaseUrl>()!!.url + "/"
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .baseUrl(API_BASE_URL + repositoryUrl)
            .build()
        retrofit.create(repositoryClass.java)
    }

    fun getAll(): PagedModel<EntityModel<E>>? {
        val call = repository.getAll()
        val response = call.execute()
        if (!response.isSuccessful) {
            throw IOException(if (response.errorBody() != null) response.errorBody()!!.string() else "Unknown error")
        }
        val returnType = typeFactory.constructParametricType(
            PagedModel::class.java,
            typeFactory.constructParametricType(
                EntityModel::class.java,
                typeFactory.constructType(dtoType)
            )
        )
        return objectMapper.readValue(response.body()?.string(), returnType)
    }

    fun deleteById(id: Long): Boolean {
        val call = repository.deleteById(id)
        val response = call.execute()
        if (!response.isSuccessful) {
            throw IOException(if (response.errorBody() != null) response.errorBody()!!.string() else "Unknown error")
        }
        return true
    }

    fun save(entity: E): E? {
        val call = repository.save(objectMapper.writeValueAsString(entity))
        val response = call.execute()
        if (!response.isSuccessful) {
            throw IOException(if (response.errorBody() != null) response.errorBody()!!.string() else "Unknown error")
        }
        val returnType = typeFactory.constructType(dtoType)
        return objectMapper.readValue(response.body()?.string(), returnType) as E?
    }
}

@Service
class BrigadeMemberService : AbstractService<BrigadeMemberRepository, BrigadeMember>(BrigadeMemberRepository::class)

@Service
class BrigadeService : AbstractService<BrigadeRepository, Brigade>(BrigadeRepository::class)

@Service
class BuildObjectService : AbstractService<BuildObjectRepository, BuildObject>(BuildObjectRepository::class)

@Service
class CustomerService : AbstractService<CustomerRepository, Customer>(CustomerRepository::class)

@Service
class EstimateService : AbstractService<EstimateRepository, Estimate>(EstimateRepository::class)

@Service
class MachineryService : AbstractService<MachineryRepository, Machinery>(MachineryRepository::class)

@Service
class MachineryModelService : AbstractService<MachineryModelRepository, MachineryModel>(MachineryModelRepository::class)

@Service
class MachineryTypeService : AbstractService<MachineryTypeRepository, MachineryType>(MachineryTypeRepository::class)

@Service
class ManagementService : AbstractService<ManagementRepository, Management>(ManagementRepository::class)

@Service
class MaterialService : AbstractService<MaterialRepository, Material>(MaterialRepository::class)

@Service
class MaterialConsumptionService :
    AbstractService<MaterialConsumptionRepository, MaterialConsumption>(MaterialConsumptionRepository::class)

@Service
class ObjectBrigadeService : AbstractService<ObjectBrigadeRepository, ObjectBrigade>(ObjectBrigadeRepository::class)

@Service
class ObjectMachineryService :
    AbstractService<ObjectMachineryRepository, ObjectMachinery>(ObjectMachineryRepository::class)

@Service
class PlotService : AbstractService<PlotRepository, Plot>(PlotRepository::class)

@Service
class PrototypeService : AbstractService<PrototypeRepository, Prototype>(PrototypeRepository::class)

@Service
class PrototypeTypeService : AbstractService<PrototypeTypeRepository, PrototypeType>(PrototypeTypeRepository::class)

@Service
class StaffService : AbstractService<StaffRepository, Staff>(StaffRepository::class)

@Service
class TitleService : AbstractService<TitleRepository, Title>(TitleRepository::class)

@Service
class TitleCategoryService : AbstractService<TitleCategoryRepository, TitleCategory>(TitleCategoryRepository::class)

@Service
class WorkScheduleService : AbstractService<WorkScheduleRepository, WorkSchedule>(WorkScheduleRepository::class)

@Service
class WorkTypeService : AbstractService<WorkTypeRepository, WorkType>(WorkTypeRepository::class)
