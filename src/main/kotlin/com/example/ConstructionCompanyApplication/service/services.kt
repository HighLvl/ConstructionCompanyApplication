package com.example.ConstructionCompanyApplication.service

import com.example.ConstructionCompanyApplication.APIConfiguration
import com.example.ConstructionCompanyApplication.APIConfiguration.Companion.API_BASE_URL
import com.example.ConstructionCompanyApplication.dto.*
import com.example.ConstructionCompanyApplication.repository.*
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType
import okhttp3.RequestBody
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule
import org.springframework.stereotype.Service
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation


abstract class AbstractService<R : AbstractRepository, E : AbstractDto> :
    APIConfiguration {
    private val dtoType: Type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
    private val repositoryClass: KClass<R> =
        ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<R>).kotlin

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

        val modelType = typeFactory.constructParametricType(
            PagedModel::class.java,
            typeFactory.constructParametricType(
                EntityModel::class.java,
                typeFactory.constructType(dtoType)
            )
        )

        return objectMapper.readValue(response.body()?.string(), modelType)
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
        val call = repository.save(toRequestBody(entity))
        val response = call.execute()
        if (!response.isSuccessful) {
            throw IOException(if (response.errorBody() != null) response.errorBody()!!.string() else "Unknown error")
        }

        val modelType = typeFactory.constructType(dtoType)
        return objectMapper.readValue(response.body()?.string(), modelType) as E?
    }

    private fun toRequestBody(dto: AbstractDto) =
        RequestBody.create(MediaType.get("application/json"), objectMapper.writeValueAsString(dto))
}

@Service
class BrigadeMemberService : AbstractService<BrigadeMemberRepository, BrigadeMember>()

@Service
class BrigadeService : AbstractService<BrigadeRepository, Brigade>()

@Service
class BuildObjectService : AbstractService<BuildObjectRepository, BuildObject>()

@Service
class CustomerService : AbstractService<CustomerRepository, Customer>()

@Service
class EstimateService : AbstractService<EstimateRepository, Estimate>()

@Service
class MachineryService : AbstractService<MachineryRepository, Machinery>()

@Service
class MachineryModelService : AbstractService<MachineryModelRepository, MachineryModel>()

@Service
class MachineryTypeService : AbstractService<MachineryTypeRepository, MachineryType>()

@Service
class ManagementService : AbstractService<ManagementRepository, Management>()

@Service
class MaterialService : AbstractService<MaterialRepository, Material>()

@Service
class MaterialConsumptionService :
    AbstractService<MaterialConsumptionRepository, MaterialConsumption>()

@Service
class ObjectBrigadeService : AbstractService<ObjectBrigadeRepository, ObjectBrigade>()

@Service
class ObjectMachineryService :
    AbstractService<ObjectMachineryRepository, ObjectMachinery>()

@Service
class PlotService : AbstractService<PlotRepository, Plot>()

@Service
class PrototypeService : AbstractService<PrototypeRepository, Prototype>()

@Service
class PrototypeTypeService : AbstractService<PrototypeTypeRepository, PrototypeType>()

@Service
class StaffService : AbstractService<StaffRepository, Staff>()

@Service
class TitleService : AbstractService<TitleRepository, Title>()

@Service
class TitleCategoryService : AbstractService<TitleCategoryRepository, TitleCategory>()

@Service
class WorkScheduleService : AbstractService<WorkScheduleRepository, WorkSchedule>()

@Service
class WorkTypeService : AbstractService<WorkTypeRepository, WorkType>()
