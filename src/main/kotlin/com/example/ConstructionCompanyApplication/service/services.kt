package com.example.ConstructionCompanyApplication.service

import com.example.ConstructionCompanyApplication.APIConfiguration
import com.example.ConstructionCompanyApplication.APIConfiguration.Companion.API_BASE_URL
import com.example.ConstructionCompanyApplication.dto.*
import com.example.ConstructionCompanyApplication.repository.*
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType
import okhttp3.RequestBody
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule


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

    fun getAll(pageable: Pageable): PagedModel<EntityModel<E>>? {
        val call = repository.getAll(convertPageableToQueryMap(pageable))
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
        val requestBody = toRequestBody(entity)
        val call = repository.save(requestBody)
        val response = call.execute()
        if (!response.isSuccessful) {
            throw IOException(if (response.errorBody() != null) response.errorBody()!!.string() else "Unknown error")
        }

        val modelType = typeFactory.constructType(dtoType)
        return objectMapper.readValue(response.body()?.string(), modelType) as E?
    }

    private fun convertPageableToQueryMap(pageable: Pageable)
    = mapOf<String, Any>("page" to pageable.pageNumber, "size" to pageable.pageSize)

    private fun toRequestBody(dto: AbstractDto) =
        RequestBody.create(MediaType.get("application/json"), objectMapper.writeValueAsString(dto))
}


class BrigadeMemberService : AbstractService<BrigadeMemberRepository, BrigadeMember>()


class BrigadeService : AbstractService<BrigadeRepository, Brigade>()


class BuildObjectService : AbstractService<BuildObjectRepository, BuildObject>()


class CustomerService : AbstractService<CustomerRepository, Customer>()


class EstimateService : AbstractService<EstimateRepository, Estimate>()


class MachineryService : AbstractService<MachineryRepository, Machinery>()


class MachineryModelService : AbstractService<MachineryModelRepository, MachineryModel>()


class MachineryTypeService : AbstractService<MachineryTypeRepository, MachineryType>()


class ManagementService : AbstractService<ManagementRepository, Management>()


class MaterialService : AbstractService<MaterialRepository, Material>()


class MaterialConsumptionService :
    AbstractService<MaterialConsumptionRepository, MaterialConsumption>()


class ObjectBrigadeService : AbstractService<ObjectBrigadeRepository, ObjectBrigade>()


class ObjectMachineryService :
    AbstractService<ObjectMachineryRepository, ObjectMachinery>()


class PlotService : AbstractService<PlotRepository, Plot>()


class PrototypeService : AbstractService<PrototypeRepository, Prototype>()


class PrototypeTypeService : AbstractService<PrototypeTypeRepository, PrototypeType>()


class StaffService : AbstractService<StaffRepository, Staff>()


class TitleService : AbstractService<TitleRepository, Title>()


class TitleCategoryService : AbstractService<TitleCategoryRepository, TitleCategory>()


class WorkScheduleService : AbstractService<WorkScheduleRepository, WorkSchedule>()


class WorkTypeService : AbstractService<WorkTypeRepository, WorkType>()
