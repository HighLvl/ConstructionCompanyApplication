package com.example.ConstructionCompanyApplication.service


import com.example.ConstructionCompanyApplication.APIConfiguration
import com.example.ConstructionCompanyApplication.APIConfiguration.Companion.API_BASE_URL
import com.example.ConstructionCompanyApplication.dto.AbstractEntity
import com.example.ConstructionCompanyApplication.repository.Repository
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import javafx.beans.property.SimpleObjectProperty
import okhttp3.MediaType
import okhttp3.RequestBody
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.IOException
import kotlin.reflect.KClass


class CommonService<T : AbstractEntity>(private val dtoClass: KClass<T>) : APIConfiguration {
    private val objectMapper = ObjectMapper().registerModules(
        Jackson2HalModule(),
        JavaTimeModule(),
        SimpleModule(
            "app",
            Version.unknownVersion(),
            mapOf(SimpleObjectProperty::class.java to SimpleObjectPropertyDeserializer()),
            listOf(SimpleObjectPropertySerializer())
        )
    )
    private val typeFactory = objectMapper.typeFactory
    private val repository: Repository = run {
        val repositoryUrl = EntityEndpointMapper.mapToEndpoint(dtoClass) + "/"
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .baseUrl(API_BASE_URL + repositoryUrl)
            .build()
        retrofit.create(Repository::class.java)
    }

    fun getAll(pageable: Pageable): PagedModel<EntityModel<T>>? {
        val query = convertPageableToQuery(pageable)
        val call = repository.getAll(query.first, query.second)
        val response = call.execute()
        if (!response.isSuccessful) {
            throw IOException(if (response.errorBody() != null) response.errorBody()!!.string() else "Unknown error")
        }

        return mapToDto(response.body()?.string())
    }

    fun deleteAll(collection: Collection<T>) {
        val call = repository.deleteAll(collection.map { it.id.value }.toList())
        val response = call.execute()
        if (!response.isSuccessful) {
            throw IOException(if (response.errorBody() != null) response.errorBody()!!.string() else "Unknown error")
        }
    }

    fun saveAll(collection: Collection<T>) {
        val call = repository.saveAll(toRequestBody(collection))
        val response = call.execute()
        if (!response.isSuccessful) {
            throw IOException(if (response.errorBody() != null) response.errorBody()!!.string() else "Unknown error")
        }
    }

    fun get(url: String, pageable: Pageable): PagedModel<EntityModel<T>>? {
        val query = convertPageableToQuery(pageable)
        val call = repository.get(url, query.first, query.second)
        val response = call.execute()
        if (!response.isSuccessful) {
            throw IOException(if (response.errorBody() != null) response.errorBody()!!.string() else "Unknown error")
        }

        return mapToDto(response.body()?.string())
    }

    fun findByRsql(pageable: Pageable, filter: String): PagedModel<EntityModel<T>>? {
        val pageableQuery = convertPageableToQuery(pageable)
        val call = repository.findByRsql(filter, pageableQuery.first, pageableQuery.second)
        val response = call.execute()
        if (!response.isSuccessful) {
            throw IOException(if (response.errorBody() != null) response.errorBody()!!.string() else "Unknown error")
        }

        return mapToDto(response.body()?.string())
    }

    private fun mapToDto(responseBodyString: String?): PagedModel<EntityModel<T>>? {
        val modelType = typeFactory.constructParametricType(
            PagedModel::class.java,
            typeFactory.constructParametricType(
                EntityModel::class.java,
                typeFactory.constructType(dtoClass.java)
            )
        )

        return objectMapper.readValue(responseBodyString, modelType)
    }

    private fun convertPageableToQuery(pageable: Pageable): Pair<Map<String, Any>, List<String>> {
        val sortList = mutableListOf<String>()
        for (o in pageable.sort) {
            sortList.add("${o.property},${o.direction.name}")
        }
        return Pair(mapOf("page" to pageable.pageNumber, "size" to pageable.pageSize), sortList)
    }

    private fun toRequestBody(collection: Collection<AbstractEntity>) =
        RequestBody.create(MediaType.get("application/json"), objectMapper.writeValueAsString(collection))
}
