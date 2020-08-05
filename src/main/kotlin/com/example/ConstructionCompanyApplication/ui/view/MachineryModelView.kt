package com.example.ConstructionCompanyApplication.ui.view

import com.example.ConstructionCompanyApplication.dto.*
import com.example.ConstructionCompanyApplication.ui.controller.*
import com.example.ConstructionCompanyApplication.ui.viewmodel.*
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.util.Callback
import org.springframework.data.domain.PageRequest
import tornadofx.*
import java.lang.reflect.ParameterizedType
import java.time.LocalDate
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance


abstract class AbstractTableView<DTO : AbstractDto, VM : AbstractItemViewModel<DTO>, C : AbstractController<DTO, VM, *>>(
    title: String,
    private val controller: C
) : View(title) {
    private val instanceClass: KClass<DTO> =
        ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<DTO>).kotlin

    override val root: BorderPane by fxml("/view/EditableTable.fxml")
    private val tableView: TableView<DTO> by fxid()
    private val pageSizeMenu: MenuButton by fxid()
    private val pageSize25: MenuItem by fxid()
    private val pageSize50: MenuItem by fxid()
    private val pageSize100: MenuItem by fxid()
    private val createEditLabel: Label by fxid()
    private val saveButton: Button by fxid()
    private val resetButton: Button by fxid()
    private val referrerListView: ListView<AbstractController.LinkInfo> by fxid()
    private val referencingListView: ListView<AbstractController.LinkInfo> by fxid()
    private val filterGridPane: GridPane by fxid()
    private val pagination: Pagination by fxid()
    private val totalElementsLabel: Label by fxid()
    private val fieldSetPane: Pane by fxid()
    private lateinit var editCellFieldset: Fieldset

    private val viewmodel = controller.model
    private var pageSize = 25

    init {
        initTableView()
        initPagination()
        initEditableFieldSet()
        initRelatedLinks()

        viewmodel.handleProperties(object : AbstractItemViewModel.PropertyHandler<DTO> {
            override fun handleStringProperty(
                property: KProperty1<DTO, SimpleStringProperty>,
                observableValue: ObservableValue<String>
            ) {
                tableView.column(property.columnName, property)
                editCellFieldset.add(field(property.columnName) {
                    textfield(observableValue)
                })
            }

            override fun handleSelectedStringProperty(
                property: KProperty1<DTO, SelectedProperty<AbstractDto, String>>,
                observableValue: ObservableValue<String>
            ) {
                tableView.column(property.columnName, property)
                editCellFieldset.add(field(property.columnName) {
                    textfield(observableValue)
                })
            }

            override fun handleLocalDateProperty(
                property: KProperty1<DTO, SimpleObjectProperty<LocalDate>>,
                observableValue: ObservableValue<LocalDate>
            ) {
                tableView.column(property.columnName, property)
            }

            override fun handleSelectedNumberProperty(
                property: KProperty1<DTO, SelectedProperty<AbstractDto, Number>>,
                observableValue: ObservableValue<Number>
            ) {
                tableView.column(property.columnName, property)
                editCellFieldset.add(field(property.columnName) {
                    textfield(observableValue)
                })
            }

            override fun handleLongProperty(
                property: KProperty1<DTO, SimpleLongProperty>,
                observableValue: ObservableValue<Long>
            ) {
                tableView.column(property.columnName, property)
                editCellFieldset.add(field(property.columnName) {
                    textfield(observableValue as ObservableValue<Number>)
                })
            }
        })
    }

    private fun initTableView() {
        viewmodel.rebindOnChange(tableView) {
            item = it ?: instanceClass.createInstance()

        }
        tableView.items = controller.dataList
    }

    private fun initEditableFieldSet() {
        editCellFieldset = fieldset {}
        fieldSetPane.add(
            form { add(editCellFieldset) }
        )

        saveButton.enableWhen(viewmodel.dirty)
        saveButton.action { save() }
        resetButton.action { viewmodel.rollback() }
    }

    private fun initPagination() {
        pagination.setPageFactory { pageIndex ->
            val pageInfo = controller.loadAll(PageRequest.of(pageIndex, pageSize))
            pagination.pageCount = pageInfo.totalPages.toInt()
            totalElementsLabel.text = pageInfo.totalElements.toString()
            tableView
        }

        fun changePageSize(pageSize: Int) {
            this.pageSize = pageSize
            pageSizeMenu.text = pageSize.toString()
        }
        pageSizeMenu.text = pageSize.toString()
        pageSize25.action { changePageSize(25) }
        pageSize50.action { changePageSize(50) }
        pageSize100.action { changePageSize(100) }
    }


    private fun initRelatedLinks() {
        referrerListView.items = SortedFilteredList(controller.tableRelatedLinksList, { it.isReferred })
        referrerListView.cellFormat {
            graphic = hyperlink(it.endpoint)
            text = "Property name: ${it.propertyName}\n" +
                    "Endpoint: ${it.endpoint}\n" +
                    "href: ${it.href}"
        }

        referencingListView.items = SortedFilteredList(controller.tableRelatedLinksList, { !it.isReferred })
        referencingListView.cellFormat {
            text = "Property name: ${it.propertyName}\n" +
                    "Endpoint: ${it.endpoint}\n" +
                    "href: ${it.href}"
        }
    }

    private fun save() {
        // Flush changes from the text fields into the model
        viewmodel.commit()

        // The edited person is contained in the model
        val person = viewmodel.item

        // A real application would persist the person here
        println("Saving ")// )/// ${person.machineryType!!.name}")
    }

    private fun <S, T> TableView<S>.column(
        title: String,
        prop: KProperty1<S, ObservableValue<T>>,
        op: TableColumn<S, T>.() -> Unit = {}
    ): TableColumn<S, T> {
        val column = TableColumn<S, T>(title)
        column.cellValueFactory = Callback { prop.call(it.value) }
        addColumnInternal(column)
        return column.also(op)
    }
}

class MachineryModelView : AbstractTableView<MachineryModel, MachineryModelViewModel, MachineryModelController>(
    "Модели строительной техники",
    MachineryModelController()
)

class BrigadeMemberView :
    AbstractTableView<BrigadeMember, BrigadeMemberViewModel, BrigadeMemberController>("", BrigadeMemberController())


class BrigadeView : AbstractTableView<Brigade, BrigadeViewModel, BrigadeController>("", BrigadeController())


class BuildObjectView :
    AbstractTableView<BuildObject, BuildObjectViewModel, BuildObjectController>("", BuildObjectController())


class CustomerView : AbstractTableView<Customer, CustomerViewModel, CustomerController>("", CustomerController())


class EstimateView : AbstractTableView<Estimate, EstimateViewModel, EstimateController>("", EstimateController())


class MachineryView : AbstractTableView<Machinery, MachineryViewModel, MachineryController>("", MachineryController())


class MachineryTypeView :
    AbstractTableView<MachineryType, MachineryTypeViewModel, MachineryTypeController>("", MachineryTypeController())


class ManagementView :
    AbstractTableView<Management, ManagementViewModel, ManagementController>("", ManagementController())


class MaterialView : AbstractTableView<Material, MaterialViewModel, MaterialController>("", MaterialController())


class MaterialConsumptionView :
    AbstractTableView<MaterialConsumption, MaterialConsumptionViewModel, MaterialConsumptionController>(
        "",
        MaterialConsumptionController()
    )


class ObjectBrigadeView :
    AbstractTableView<ObjectBrigade, ObjectBrigadeViewModel, ObjectBrigadeController>("", ObjectBrigadeController())


class ObjectMachineryView : AbstractTableView<ObjectMachinery, ObjectMachineryViewModel, ObjectMachineryController>(
    "",
    ObjectMachineryController()
)


class PlotView : AbstractTableView<Plot, PlotViewModel, PlotController>("", PlotController())


class PrototypeView : AbstractTableView<Prototype, PrototypeViewModel, PrototypeController>("", PrototypeController())


class PrototypeTypeView :
    AbstractTableView<PrototypeType, PrototypeTypeViewModel, PrototypeTypeController>("", PrototypeTypeController())


class StaffView : AbstractTableView<Staff, StaffViewModel, StaffController>("", StaffController())


class TitleView : AbstractTableView<Title, TitleViewModel, TitleController>("", TitleController())


class TitleCategoryView :
    AbstractTableView<TitleCategory, TitleCategoryViewModel, TitleCategoryController>("", TitleCategoryController())


class WorkScheduleView :
    AbstractTableView<WorkSchedule, WorkScheduleViewModel, WorkScheduleController>("", WorkScheduleController())


class WorkTypeView : AbstractTableView<WorkType, WorkTypeViewModel, WorkTypeController>("", WorkTypeController())
