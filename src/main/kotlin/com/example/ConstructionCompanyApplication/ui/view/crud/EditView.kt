package com.example.ConstructionCompanyApplication.ui.view.crud

import com.example.ConstructionCompanyApplication.dto.AbstractEntity
import com.example.ConstructionCompanyApplication.service.EntityEndpointMapper
import com.example.ConstructionCompanyApplication.ui.configuration.EntityConfigurationProvider
import com.example.ConstructionCompanyApplication.ui.controller.CommonController
import com.example.ConstructionCompanyApplication.ui.view.DeleteTableColumn
import com.example.ConstructionCompanyApplication.ui.view.EntityTableView
import com.example.ConstructionCompanyApplication.ui.view.MainTabPane
import com.example.ConstructionCompanyApplication.ui.view.SortBox
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import org.springframework.data.domain.PageRequest
import tornadofx.*
import tornadofx.control.DatePickerTableCell
import java.time.LocalDate
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1


@Suppress("UNCHECKED_CAST")
class EditView<T : AbstractEntity>(
    entityClass: KClass<T>
) : View(EntityConfigurationProvider.get(entityClass).entityMetadata.name) {
    override val root: BorderPane by fxml("/view/EditableTable.fxml")
    private val pageSizeMenu: MenuButton by fxid()
    private val pageSize25: MenuItem by fxid()
    private val pageSize50: MenuItem by fxid()
    private val pageSize100: MenuItem by fxid()
    private val addElementButton: Button by fxid()
    private val referrerListView: ListView<CommonController.LinkInfo> by fxid()
    private val referencingListView: ListView<CommonController.LinkInfo> by fxid()
    private val filterGridPane: GridPane by fxid()
    private val pagination: Pagination by fxid()
    private val totalElementsLabel: Label by fxid()
    private val saveButton: Button by fxid()
    private val cancelButton: Button by fxid()
    private val showAllButton: Button by fxid()
    private val refreshButton: Button by fxid()
    private val topVBox: VBox by fxid()

    private val tableView = EntityConfigurationProvider.get(entityClass).tableViewFactory.create() as EntityTableView<T>
    private val controller = CommonController(entityClass)

    private val createView = CreateView(entityClass)

    private var dataSourceUrlProperty = SimpleStringProperty("")
    private val toDeleteList = observableListOf<T>()

    private var tableViewEditModel: TableViewEditModel<T> by singleAssign()

    private val tableViewPagination = EntityTableViewPagination(
        pagination,
        tableView,
        pageSizeMenu,
        pageSize25,
        pageSize50,
        pageSize100,
        totalElementsLabel
    )
    private val sortBox = SortBox(tableView)

    init {
        initTableView()
        initPagination()
        initRelatedLinks()
        initSaveAndCancelButtons()
        initAddElementsButton()
        initShowAllButton()
        initRefreshButton()
        initSortBox()
    }

    private fun initRefreshButton() {
        refreshButton.action {
            tableViewPagination.loadPage(pagination.currentPageIndex)
        }
    }

    private fun initSortBox() {
        topVBox.add(sortBox)
    }

    private fun initShowAllButton() {
        showAllButton.action { dataSourceUrlProperty.set("") }
        showAllButton.visibleWhen { dataSourceUrlProperty.isNotEmpty }
        dataSourceUrlProperty.addListener { _, _, newValue ->
            if (newValue.isEmpty()) {
                tableViewPagination.loadPage(0)
            }
        }
    }

    fun setDataSource(url: String) {
        dataSourceUrlProperty.set(url)
    }

    private val editNode: Node = root.center.getChildList()!![0]

    private enum class ViewState { EDIT, CREATE }

    private var viewState = ViewState.EDIT
    private fun initAddElementsButton() {
        addElementButton.action {
            root.center {
                viewState = when (viewState) {
                    ViewState.CREATE -> {
                        addElementButton.text = "Добавить записи"
                        refreshButton.isVisible = true
                        sortBox.isVisible = true
                        add(editNode)
                        ViewState.EDIT
                    }
                    ViewState.EDIT -> {
                        addElementButton.text = "Редактировать таблицу"
                        refreshButton.isVisible = false
                        sortBox.isVisible = false
                        add(createView)
                        ViewState.CREATE
                    }
                }
            }
        }
    }

    private fun initTableView() {
        tableView.columnResizePolicy = SmartResize.POLICY
        tableView.isEditable = true
        tableView.items = controller.dataList
        tableView.isEditable = true
        tableView.enableCellEditing()
        tableView.enableDirtyTracking()
        tableView.addColumnInternal(
            DeleteTableColumn(
                toDeleteList
            )
        )
        tableViewEditModel = tableView.editModel

        tableView.handleProperties(object :
            EntityTableView.EditablePropertyHandler<T> {
            override fun handleIntegerProperty(name: String, property: KProperty1<T, ObjectProperty<Int>>) {
                val column = tableView.getColumnBy(property) as TableColumn<T, Int>
                column.makeEditable()
            }

            override fun handleLongProperty(name: String, property: KProperty1<T, ObjectProperty<Long>>) {
                val column = tableView.getColumnBy(property) as TableColumn<T, Long>
                column.makeEditable()
            }

            override fun handleBooleanProperty(name: String, property: KProperty1<T, ObjectProperty<Boolean>>) {
                val column = tableView.getColumnBy(property) as TableColumn<T, Boolean>
                column.makeEditable()
            }

            override fun handleStringProperty(name: String, property: KProperty1<T, ObjectProperty<String>>) {
                val column = tableView.getColumnBy(property) as TableColumn<T, String>
                column.makeEditable()
            }

            override fun handleLocalDateProperty(name: String, property: KProperty1<T, ObjectProperty<LocalDate>>) {
                val column = tableView.getColumnBy(property) as TableColumn<T, LocalDate>
                column.cellFactory = DatePickerTableCell.forTableColumn()
            }

            override fun handleEntityProperty(name: String, property: KProperty1<T, ObjectProperty<AbstractEntity>>) {
                val column = tableView.getColumnBy(property)
                column!!.setOnEditStart {
                    val entityClass = property.returnType.arguments[0].type!!.classifier as KClass<T>
                    val selectView =
                        SelectView(
                            entityClass
                        )
                    val selectedEntity = selectView.select() ?: return@setOnEditStart

                    val items = tableViewEditModel.items
                    if (it.rowValue !in items) {
                        val dirtyState = TableColumnDirtyState(tableViewEditModel, it.rowValue)
                        items[it.rowValue] = dirtyState
                    }

                    if (column !in items[it.rowValue]!!.dirtyColumns)
                        items[it.rowValue]!!.dirtyColumns[column as TableColumn<T, Any?>] =
                            property.get(it.rowValue).get() as Any?

                    property.get(it.rowValue).set(selectedEntity)
                }

            }
        })
    }

    private fun initPagination() {
        tableViewPagination.pageLoader = { pageIndex, pageSize ->
            controller.loadAll(
                PageRequest.of(pageIndex, pageSize, sortBox.sort),
                dataSourceUrlProperty.value
            )
        }
    }


    private fun initRelatedLinks() {
        fun setupReferencingTableLinks() {
            referencingListView.items = SortedFilteredList(controller.tableRelatedLinksList, { !it.isReferred })
            referencingListView.cellFormat {
                val referencingClass = EntityEndpointMapper.mapToEntityClass(it.endpoint)
                val hyperlink = hyperlink(EntityConfigurationProvider.get(referencingClass).entityMetadata.name)
                hyperlink.action {
                    addTab(referencingClass)
                }
                graphic = hyperlink
            }
        }

        fun setupReferrerTableLinks() {
            referrerListView.items = SortedFilteredList(controller.tableRelatedLinksList, { it.isReferred })
            referrerListView.cellFormat {
                val columnName = tableView.getColumnNameBy(it.propertyName)
                val hyperlink = hyperlink(columnName)
                hyperlink.action {
                    addTab(EntityEndpointMapper.mapToEntityClass(it.endpoint), info=columnName)
                }
                graphic = hyperlink
            }
        }

        fun setupReferrerItemLinks(item: T) {
            val relatedLinks = controller.itemRelatedLinksMap[item] ?: return
            referrerListView.items = SortedFilteredList(relatedLinks, { it.isReferred })

            referrerListView.cellFormat {
                val columnName = tableView.getColumnNameBy(it.propertyName)
                val hyperlink = hyperlink(columnName)
                hyperlink.action {
                    addTab(EntityEndpointMapper.mapToEntityClass(it.endpoint), it.url, columnName)
                }
                graphic = hyperlink
            }
        }

        fun setupReferencingItemLinks(item: T) {
            val relatedLinks = controller.itemRelatedLinksMap[item] ?: return
            referencingListView.items = SortedFilteredList(relatedLinks, { !it.isReferred })

            referencingListView.cellFormat {
                val referencingClass = EntityEndpointMapper.mapToEntityClass(it.endpoint)
                val hyperlink = hyperlink(EntityConfigurationProvider.get(referencingClass).entityMetadata.name)
                hyperlink.action {
                    addTab(referencingClass, it.url)
                }
                graphic = hyperlink
            }
        }

        tableView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            if (newValue == null) {
                setupReferencingTableLinks()
                referencingListView.refresh()
                setupReferrerTableLinks()
                referencingListView.refresh()
                return@addListener
            }

            setupReferencingItemLinks(newValue)
            referencingListView.refresh()

            setupReferrerItemLinks(newValue)
            referrerListView.refresh()
        }

        setupReferrerTableLinks()
        setupReferencingTableLinks()
    }

    private val tabPane by lazy {
        var tabPane: MainTabPane? = null

        var node: Node? = root.parent
        while (node != null && tabPane == null) {
            if (node is MainTabPane) {
                tabPane = node as MainTabPane
            }
            node = node!!.parent
        }

        tabPane!!
    }

    private fun addTab(entityClass: KClass<out AbstractEntity>, dataSource: String? = null, info: String? = null) {
        tabPane.tab(entityClass, dataSource, info)
    }

    private fun initSaveAndCancelButtons() {
        saveButton.action { save() }
        cancelButton.action { cancel() }

    }

    private fun save() {
        controller.deleteAll(toDeleteList)
        val saveList =
            tableViewEditModel.items.asSequence().filter { it.value.isDirty && it.key !in toDeleteList }.map { it.key }
                .toList()
        controller.saveAll(saveList)
        tableViewEditModel.commit()
        toDeleteList.clear()

        tableViewPagination.loadPage(pagination.currentPageIndex)
    }

    private fun cancel() {
        tableViewEditModel.rollback()
        toDeleteList.clear()
    }
}