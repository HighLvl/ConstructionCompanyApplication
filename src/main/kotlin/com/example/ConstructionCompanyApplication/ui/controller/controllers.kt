package com.example.ConstructionCompanyApplication.ui.controller

import com.example.ConstructionCompanyApplication.dto.*
import com.example.ConstructionCompanyApplication.service.*
import com.example.ConstructionCompanyApplication.ui.viewmodel.*
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.Link
import org.springframework.hateoas.Links
import tornadofx.*

abstract class AbstractController<DTO : AbstractDto, VM : AbstractItemViewModel<DTO>, S : AbstractService<*, DTO>>(
    val model: VM,
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
        }.toMap() as Map<DTO, List<AbstractController.LinkInfo>>)
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

class MachineryModelController : AbstractController<MachineryModel, MachineryModelViewModel, MachineryModelService>(
    MachineryModelViewModel(),
    MachineryModelService()
)

class BrigadeMemberController : AbstractController<BrigadeMember, BrigadeMemberViewModel, BrigadeMemberService>(
    BrigadeMemberViewModel(),
    BrigadeMemberService()
)


class BrigadeController :
    AbstractController<Brigade, BrigadeViewModel, BrigadeService>(BrigadeViewModel(), BrigadeService())


class BuildObjectController : AbstractController<BuildObject, BuildObjectViewModel, BuildObjectService>(
    BuildObjectViewModel(),
    BuildObjectService()
)


class CustomerController :
    AbstractController<Customer, CustomerViewModel, CustomerService>(CustomerViewModel(), CustomerService())


class EstimateController :
    AbstractController<Estimate, EstimateViewModel, EstimateService>(EstimateViewModel(), EstimateService())


class MachineryController :
    AbstractController<Machinery, MachineryViewModel, MachineryService>(MachineryViewModel(), MachineryService())


class MachineryTypeController : AbstractController<MachineryType, MachineryTypeViewModel, MachineryTypeService>(
    MachineryTypeViewModel(),
    MachineryTypeService()
)


class ManagementController :
    AbstractController<Management, ManagementViewModel, ManagementService>(ManagementViewModel(), ManagementService())


class MaterialController :
    AbstractController<Material, MaterialViewModel, MaterialService>(MaterialViewModel(), MaterialService())


class MaterialConsumptionController :
    AbstractController<MaterialConsumption, MaterialConsumptionViewModel, MaterialConsumptionService>(
        MaterialConsumptionViewModel(),
        MaterialConsumptionService()
    )


class ObjectBrigadeController : AbstractController<ObjectBrigade, ObjectBrigadeViewModel, ObjectBrigadeService>(
    ObjectBrigadeViewModel(),
    ObjectBrigadeService()
)


class ObjectMachineryController : AbstractController<ObjectMachinery, ObjectMachineryViewModel, ObjectMachineryService>(
    ObjectMachineryViewModel(),
    ObjectMachineryService()
)


class PlotController : AbstractController<Plot, PlotViewModel, PlotService>(PlotViewModel(), PlotService())


class PrototypeController :
    AbstractController<Prototype, PrototypeViewModel, PrototypeService>(PrototypeViewModel(), PrototypeService())


class PrototypeTypeController : AbstractController<PrototypeType, PrototypeTypeViewModel, PrototypeTypeService>(
    PrototypeTypeViewModel(),
    PrototypeTypeService()
)


class StaffController : AbstractController<Staff, StaffViewModel, StaffService>(StaffViewModel(), StaffService())


class TitleController : AbstractController<Title, TitleViewModel, TitleService>(TitleViewModel(), TitleService())


class TitleCategoryController : AbstractController<TitleCategory, TitleCategoryViewModel, TitleCategoryService>(
    TitleCategoryViewModel(),
    TitleCategoryService()
)


class WorkScheduleController : AbstractController<WorkSchedule, WorkScheduleViewModel, WorkScheduleService>(
    WorkScheduleViewModel(),
    WorkScheduleService()
)


class WorkTypeController :
    AbstractController<WorkType, WorkTypeViewModel, WorkTypeService>(WorkTypeViewModel(), WorkTypeService())


