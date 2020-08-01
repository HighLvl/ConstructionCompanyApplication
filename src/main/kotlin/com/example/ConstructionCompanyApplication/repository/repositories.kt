package com.example.ConstructionCompanyApplication.repository

import com.example.ConstructionCompanyApplication.dto.AbstractDto
import okhttp3.Response
import okhttp3.ResponseBody
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import retrofit2.Call
import retrofit2.http.*

@Target(AnnotationTarget.CLASS)
annotation class BaseUrl(val url: String)

interface AbstractRepository {
    @GET(".")
    fun getAll(): Call<ResponseBody>

    @POST(".")
    fun save(@Body entity: String): Call<ResponseBody>

    @DELETE("{id}")
    fun deleteById(@Path("id") id: Long): Call<ResponseBody>


}


@BaseUrl("/brigadeMembers")
interface BrigadeMemberRepository : AbstractRepository

@BaseUrl("/brigades")
interface BrigadeRepository : AbstractRepository

@BaseUrl("/buildObjects")
interface BuildObjectRepository : AbstractRepository

@BaseUrl("/customers")
interface CustomerRepository : AbstractRepository

@BaseUrl("/estimates")
interface EstimateRepository : AbstractRepository

@BaseUrl("/machines")
interface MachineryRepository : AbstractRepository

@BaseUrl("/machineryModels")
interface MachineryModelRepository : AbstractRepository

@BaseUrl("/machineryTypes")
interface MachineryTypeRepository : AbstractRepository

@BaseUrl("/managements")
interface ManagementRepository : AbstractRepository

@BaseUrl("/materials")
interface MaterialRepository : AbstractRepository

@BaseUrl("/materialConsumptions")
interface MaterialConsumptionRepository : AbstractRepository

@BaseUrl("/objectBrigades")
interface ObjectBrigadeRepository : AbstractRepository

@BaseUrl("/objectMachines")
interface ObjectMachineryRepository : AbstractRepository

@BaseUrl("/plots")
interface PlotRepository : AbstractRepository

@BaseUrl("/prototypes")
interface PrototypeRepository : AbstractRepository

@BaseUrl("/prototypeTypes")
interface PrototypeTypeRepository : AbstractRepository

@BaseUrl("/staff")
interface StaffRepository : AbstractRepository

@BaseUrl("/titles")
interface TitleRepository : AbstractRepository

@BaseUrl("/titleCategories")
interface TitleCategoryRepository : AbstractRepository

@BaseUrl("/workSchedules")
interface WorkScheduleRepository : AbstractRepository

@BaseUrl("/workTypes")
interface WorkTypeRepository : AbstractRepository