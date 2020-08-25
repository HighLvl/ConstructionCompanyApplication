package com.example.ConstructionCompanyApplication.ui.view.crud

import com.example.ConstructionCompanyApplication.controller.CommonController
import com.example.ConstructionCompanyApplication.dto.AbstractEntity
import com.example.ConstructionCompanyApplication.ui.configuration.EntityConfigurationProvider
import com.example.ConstructionCompanyApplication.ui.view.EntityTableView
import com.example.ConstructionCompanyApplication.ui.view.EntityTableViewPagination
import com.example.ConstructionCompanyApplication.ui.view.SortBox
import com.example.ConstructionCompanyApplication.ui.view.setProgressIndicator
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.stage.Modality
import javafx.stage.Stage
import org.springframework.data.domain.PageRequest
import tornadofx.*
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class SelectView<T : AbstractEntity>(private val entityClass: KClass<T>) :
    View(EntityConfigurationProvider.get(entityClass).entityMetadata.name) {
    override val root: StackPane by fxml("/view/SelectView.fxml")
    private val selectButton: Button by fxid()
    private val pagination: Pagination by fxid()
    private val pageSizeMenu: MenuButton by fxid()
    private val pageSize25: MenuItem by fxid()
    private val pageSize50: MenuItem by fxid()
    private val pageSize100: MenuItem by fxid()
    private val totalElementsLabel: Label by fxid()
    private val filterPane: AnchorPane by fxid()
    private val sortHBox: HBox by fxid()

    private val tableView = EntityConfigurationProvider.get(entityClass).tableViewFactory.create() as EntityTableView<T>
    private val sortBox = SortBox(tableView)

    private val stage = Stage()
    private val controller = CommonController(entityClass)
    private val itemViewModel = ItemViewModel<T>()

    private val tableViewPagination =
        EntityTableViewPagination(
            pagination,
            tableView,
            pageSizeMenu,
            pageSize25,
            pageSize50,
            pageSize100,
            totalElementsLabel
        )

    private val selectedEntityProperty = SimpleObjectProperty<T>()

    init {
        initTableView()
        initPagination()
        initSelectButton()
        initStage()
        initSortHBox()
        initSelectButton()
        initFilterPane()
    }

    private fun initFilterPane() {
        val filterViewFactory = EntityConfigurationProvider.get(entityClass).filterViewFactory ?: return
        val filterView = filterViewFactory.create()
        filterView.onSearchRequest = {
            controller.filter = it
            tableViewPagination.loadPage(0)
        }
        filterPane.add(filterView)
    }

    fun select(): AbstractEntity? {
        tableViewPagination.loadPage(0)
        stage.showAndWait()
        val selectedEntity = selectedEntityProperty.value
        selectedEntityProperty.value = null
        return selectedEntity
    }

    private fun initSortHBox() {
        sortHBox.add(sortBox)
        sortBox.directionProperty.addListener { _, _, _ ->
            sortBox.selectColumnProperty.value ?: return@addListener
            tableViewPagination.loadPage(pagination.currentPageIndex)
        }
        sortBox.selectColumnProperty.addListener { _, _, _ ->
            sortBox.directionProperty.value ?: return@addListener
            tableViewPagination.loadPage(pagination.currentPageIndex)
        }
    }

    private fun initStage() {
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.scene = Scene(root)
        stage.title = title
    }

    private fun initTableView() {
        tableView.columnResizePolicy = SmartResize.POLICY
        tableView.items = controller.dataList
        itemViewModel.rebindOnChange(tableView) { selectedEntity ->
            item = selectedEntity
        }
    }

    private fun initPagination() {
        tableViewPagination.loadPage = { pageIndex, pageSize ->
            controller.loadAll(PageRequest.of(pageIndex, pageSize, sortBox.sort))
                .setProgressIndicator(root)
        }
    }

    private fun initSelectButton() {
        selectButton.disableWhen(itemViewModel.empty)
        selectButton.action {
            selectedEntityProperty.set(itemViewModel.item)
            stage.close()
        }
    }
}
