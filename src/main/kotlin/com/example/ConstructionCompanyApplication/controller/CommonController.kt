package com.example.ConstructionCompanyApplication.controller

import com.example.ConstructionCompanyApplication.dto.AbstractEntity
import com.example.ConstructionCompanyApplication.service.CommonService
import javafx.collections.ObservableList
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.Links
import org.springframework.hateoas.PagedModel
import tornadofx.*
import kotlin.reflect.KClass

class CommonController<T : AbstractEntity>(entityClass: KClass<T>) : Controller() {
    val dataList = observableListOf<T>()
    val itemRelatedLinksMap = observableMapOf<T, ObservableList<LinkInfo>>()
    val tableRelatedLinksList = observableListOf<LinkInfo>()
    var filter: String = ""

    private val service = CommonService(entityClass)

    fun loadAll(pageable: Pageable, url: String = ""): PageInfo {
        val result = if (filter.isEmpty())
            if (url.isEmpty()) service.getAll(pageable) else service.get(url, pageable)
        else
            service.findByRsql(pageable, filter)

        return updateDataWith(result)
    }

    @Suppress("UNCHECKED_CAST")
    private fun updateDataWith(result: PagedModel<EntityModel<T>>?): PageInfo {
        fun toLinkInfoList(links: Links) = links.toList().mapNotNull { toLinkInfo(it) }.asObservable()

        dataList.setAll(result?.content!!.map { it.content!! } as MutableCollection<T>)

        itemRelatedLinksMap.clear()
        itemRelatedLinksMap.putAll(result.content.map { entityModel ->
            entityModel.content to toLinkInfoList(entityModel.links)
        }.toMap() as Map<T, ObservableList<LinkInfo>>)

        tableRelatedLinksList.setAll(toLinkInfoList(result.links))
        return PageInfo(result.metadata!!.totalElements, result.metadata!!.totalPages)
    }

    fun deleteAll(collection: Collection<T>) {
        if (collection.isEmpty()) return
        service.deleteAll(collection)
    }

    fun saveAll(collection: Collection<T>) {
        if (collection.isEmpty()) return
        service.saveAll(collection)
    }

    private fun toLinkInfo(link: Link): LinkInfo? {
        val propertyName: String
        val endpoint: String
        val isReferred: Boolean
        val url = link.href

        val relation = link.rel.value()
        when {
            relation.startsWith("referred_") -> {
                isReferred = true
                propertyName = relation.split('_')[1]

            }
            relation.startsWith("referencing_") -> {
                isReferred = false
                propertyName = relation.split('_')[2]
            }
            else -> {
                return null
            }
        }
        endpoint = Regex("/\\w+").findAll(url).elementAt(1).value
        return LinkInfo(propertyName, url, endpoint, isReferred)
    }


    class LinkInfo(val propertyName: String, val url: String, val endpoint: String, val isReferred: Boolean)


    class PageInfo(val totalElements: Long, val totalPages: Long)
}