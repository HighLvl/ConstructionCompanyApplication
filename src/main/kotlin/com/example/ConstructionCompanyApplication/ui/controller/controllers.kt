package com.example.ConstructionCompanyApplication.ui.controller

import com.example.ConstructionCompanyApplication.dto.*
import com.example.ConstructionCompanyApplication.service.*
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.Link
import org.springframework.hateoas.Links
import tornadofx.*

abstract class AbstractController<DTO : AbstractDto, S : AbstractService<*, DTO>>(
    private val service: S
) : Controller() {


    val dataList = observableListOf<DTO>()
    val itemRelatedLinksMap = observableMapOf<DTO, List<LinkInfo>>()
    val tableRelatedLinksList = observableListOf<LinkInfo>()

    @Suppress("UNCHECKED_CAST")
    fun loadAll(pageable: Pageable): PageInfo {
        fun toLinkInfoList(links: Links) = links.toList().mapNotNull { toLinkInfo(it) }
        val result = service.getAll(pageable)
        dataList.setAll(result?.content!!.map { it.content!! } as MutableCollection<DTO>)
        itemRelatedLinksMap.clear()
        itemRelatedLinksMap.putAll(result.content.map { entityModel ->
            entityModel.content to toLinkInfoList(entityModel.links)
        }.toMap() as Map<DTO, List<LinkInfo>>)
        tableRelatedLinksList.setAll(toLinkInfoList(result.links))
        return PageInfo(result.metadata!!.totalElements, result.metadata!!.totalPages)
    }

    private fun toLinkInfo(link: Link): LinkInfo? {
        val propertyName: String
        val endpoint: String
        val isReferred: Boolean
        val href = link.href

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
        endpoint = Regex("/\\w+").findAll(href).elementAt(1).value
        return LinkInfo(propertyName, href, endpoint, isReferred)
    }

    class LinkInfo(val propertyName: String, val href: String, val endpoint: String, val isReferred: Boolean)


    class PageInfo(val totalElements: Long, val totalPages: Long)
}

class MachineryModelController : AbstractController<MachineryModel, MachineryModelService>(

    MachineryModelService()
)

class BrigadeMemberController : AbstractController<BrigadeMember, BrigadeMemberService>(

    BrigadeMemberService()
)


class BrigadeController :
    AbstractController<Brigade, BrigadeService>(BrigadeService())


class BuildObjectController : AbstractController<BuildObject, BuildObjectService>(

    BuildObjectService()
)


class CustomerController :
    AbstractController<Customer, CustomerService>(CustomerService())


class EstimateController :
    AbstractController<Estimate, EstimateService>(EstimateService())


class MachineryController :
    AbstractController<Machinery, MachineryService>(MachineryService())


class MachineryTypeController : AbstractController<MachineryType, MachineryTypeService>(

    MachineryTypeService()
)


class ManagementController :
    AbstractController<Management, ManagementService>(ManagementService())


class MaterialController :
    AbstractController<Material, MaterialService>(MaterialService())


class MaterialConsumptionController :
    AbstractController<MaterialConsumption, MaterialConsumptionService>(

        MaterialConsumptionService()
    )


class ObjectBrigadeController : AbstractController<ObjectBrigade, ObjectBrigadeService>(

    ObjectBrigadeService()
)


class ObjectMachineryController : AbstractController<ObjectMachinery, ObjectMachineryService>(

    ObjectMachineryService()
)


class PlotController : AbstractController<Plot, PlotService>(PlotService())


class PrototypeController :
    AbstractController<Prototype, PrototypeService>(PrototypeService())


class PrototypeTypeController : AbstractController<PrototypeType, PrototypeTypeService>(

    PrototypeTypeService()
)


class StaffController : AbstractController<Staff, StaffService>(StaffService())


class TitleController : AbstractController<Title, TitleService>(TitleService())


class TitleCategoryController : AbstractController<TitleCategory, TitleCategoryService>(

    TitleCategoryService()
)


class WorkScheduleController : AbstractController<WorkSchedule, WorkScheduleService>(

    WorkScheduleService()
)


class WorkTypeController :
    AbstractController<WorkType, WorkTypeService>(WorkTypeService())


