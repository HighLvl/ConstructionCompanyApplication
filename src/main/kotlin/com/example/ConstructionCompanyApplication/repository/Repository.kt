package com.example.ConstructionCompanyApplication.repository

import okhttp3.RequestBody
import okhttp3.ResponseBody


import retrofit2.Call
import retrofit2.http.*

/*Репозиторий со всеми необходимыми операциями для работы с данными*/
@JvmSuppressWildcards
interface Repository {
    @GET(".")
    fun getAll(@QueryMap pageQueryMap: Map<String, Any>, @Query("sort") sortList: List<String>): Call<ResponseBody>

    @PUT("collection")
    fun saveAll(@Body entityCollection: RequestBody): Call<ResponseBody>

    @GET
    fun get(@Url url: String, @QueryMap pageQueryMap: Map<String, Any>, @Query("sort") sortList: List<String>): Call<ResponseBody>

    @DELETE("collection")
    fun deleteAll(@Query("id") collection: Collection<Long>): Call<ResponseBody>

    @GET("search")
    fun findByRsql(@Query("filter") filter: String, @QueryMap pageQueryMap: Map<String, Any>, @Query("sort") sortList: List<String>): Call<ResponseBody>
}