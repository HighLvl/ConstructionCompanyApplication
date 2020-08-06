package com.example.ConstructionCompanyApplication.ui.view

import com.example.ConstructionCompanyApplication.dto.*
import com.example.ConstructionCompanyApplication.ui.controller.*
import javafx.beans.property.*
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import org.springframework.data.domain.PageRequest
import tornadofx.*
import tornadofx.control.DatePickerTableCell
import java.lang.reflect.ParameterizedType
import java.time.LocalDate
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

abstract class AbstractTableView<DTO : AbstractDto, C : AbstractController<DTO, *>>(
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
    private val addElementsButton: Button by fxid()
    private val referrerListView: ListView<AbstractController.LinkInfo> by fxid()
    private val referencingListView: ListView<AbstractController.LinkInfo> by fxid()
    private val filterGridPane: GridPane by fxid()
    private val pagination: Pagination by fxid()
    private val totalElementsLabel: Label by fxid()
    private lateinit var editCellFieldset: Fieldset

    private var pageSize = 25

    var tableViewEditModel: TableViewEditModel<DTO> by singleAssign()

    init {
        initTableView()
        initPagination()
        initRelatedLinks()
    }

    private fun initTableView() {
        TableViewColumnManager(tableView, instanceClass).handleProperties(object :
            TableViewColumnManager.ColumnHandler<DTO> {
            override fun handleIntegerColumn(
                property: KProperty1<DTO, SimpleIntegerProperty>,
                column: TableColumn<DTO, Int>
            ) {
                column.makeEditable()
            }

            override fun handleLongColumn(
                property: KProperty1<DTO, SimpleLongProperty>,
                column: TableColumn<DTO, Long>
            ) {
                column.makeEditable()
            }

            override fun handleBooleanColumn(
                property: KProperty1<DTO, SimpleBooleanProperty>,
                column: TableColumn<DTO, Boolean>
            ) {
            }

            override fun handleStringColumn(
                property: KProperty1<DTO, SimpleStringProperty>,
                column: TableColumn<DTO, String>
            ) {
                column.makeEditable()
            }

            override fun handleLocalDateColumn(
                property: KProperty1<DTO, SimpleObjectProperty<LocalDate>>,
                column: TableColumn<DTO, LocalDate>
            ) {
                column.cellFactory = DatePickerTableCell.forTableColumn()
            }

            override fun handleSelectedStringColumn(
                property: KProperty1<DTO, SelectedProperty<AbstractDto, String>>,
                column: TableColumn<DTO, String>
            ) {

            }

            override fun handleSelectedNumberColumn(
                property: KProperty1<DTO, SelectedProperty<AbstractDto, Number>>,
                column: TableColumn<DTO, Number>
            ) {

            }


        })

        tableView.columnResizePolicy = SmartResize.POLICY
        tableView.isEditable = true
        tableView.items = controller.dataList
        tableView.enableCellEditing()
        tableView.enableDirtyTracking()
        tableView.addColumnInternal(buildDeleteColumn())
    }

    private fun buildDeleteColumn(): TableColumn<DTO, DTO> {
        val deleteColumn = TableColumn<DTO, DTO>("")
        deleteColumn.setCellValueFactory {
            ReadOnlyObjectWrapper(it.value)
        }
        deleteColumn.minWidth = 70.0
        deleteColumn.cellFormat {
            val deleteButton = Button("X")
            deleteButton.prefWidth = 50.0
            deleteButton.style { textFill = Color.DARKRED }
            deleteButton.action { onClickDeleteButton(tableRow) }

            graphic = deleteButton
            minHeight = 50.0
            style { alignment = Pos.CENTER }
        }
        return deleteColumn
    }

    private fun onClickDeleteButton(tableRow: TableRow<DTO>) {
        tableRow.style {
            //backgroundColor += Color.GRAY
        }
        //controller.dataList.remove(tableRow.item)
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

        // A real application would persist the person here
        println("Saving ")// )/// ${person.machineryType!!.name}")
    }
}

class MachineryModelView : AbstractTableView<MachineryModel, MachineryModelController>(
    "Модели строительной техники",
    MachineryModelController()
)

class BrigadeMemberView :
    AbstractTableView<BrigadeMember, BrigadeMemberController>("", BrigadeMemberController())


class BrigadeView : AbstractTableView<Brigade, BrigadeController>("", BrigadeController())


class BuildObjectView :
    AbstractTableView<BuildObject, BuildObjectController>("", BuildObjectController())


class CustomerView : AbstractTableView<Customer, CustomerController>("", CustomerController())


class EstimateView : AbstractTableView<Estimate, EstimateController>("", EstimateController())


class MachineryView : AbstractTableView<Machinery, MachineryController>("", MachineryController())


class MachineryTypeView :
    AbstractTableView<MachineryType, MachineryTypeController>("", MachineryTypeController())


class ManagementView :
    AbstractTableView<Management, ManagementController>("", ManagementController())


class MaterialView : AbstractTableView<Material, MaterialController>("", MaterialController())


class MaterialConsumptionView :
    AbstractTableView<MaterialConsumption, MaterialConsumptionController>(
        "",
        MaterialConsumptionController()
    )


class ObjectBrigadeView :
    AbstractTableView<ObjectBrigade, ObjectBrigadeController>("", ObjectBrigadeController())


class ObjectMachineryView : AbstractTableView<ObjectMachinery, ObjectMachineryController>(
    "",
    ObjectMachineryController()
)


class PlotView : AbstractTableView<Plot, PlotController>("", PlotController())


class PrototypeView : AbstractTableView<Prototype, PrototypeController>("", PrototypeController())


class PrototypeTypeView :
    AbstractTableView<PrototypeType, PrototypeTypeController>("", PrototypeTypeController())


class StaffView : AbstractTableView<Staff, StaffController>("", StaffController())


class TitleView : AbstractTableView<Title, TitleController>("", TitleController())


class TitleCategoryView :
    AbstractTableView<TitleCategory, TitleCategoryController>("", TitleCategoryController())


class WorkScheduleView :
    AbstractTableView<WorkSchedule, WorkScheduleController>("", WorkScheduleController())


class WorkTypeView : AbstractTableView<WorkType, WorkTypeController>("", WorkTypeController())
