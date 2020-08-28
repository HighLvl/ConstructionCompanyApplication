package com.example.ConstructionCompanyApplication

import com.example.ConstructionCompanyApplication.ui.configuration.EntityConfigurationProvider
import com.example.ConstructionCompanyApplication.ui.view.MainTabPane
import javafx.scene.control.TreeItem
import javafx.stage.Stage
import tornadofx.*

class StartView : View("Строительная организация") {
    private val tabPane = MainTabPane()

    override val root = borderpane {
        left {
            val actionMap = mutableMapOf<TreeItem<*>, () -> Unit>()
            val rootTreeItem = TreeItem("")

            val tableTreeItem = TreeItem("Таблицы")
            tableTreeItem.isExpanded = true
            for ((groupName, entityList) in EntityConfigurationProvider.mapOfEntitiesByGroup) {
                val childItem = TreeItem(groupName)
                entityList.forEach {
                    val nestedItem = TreeItem(EntityConfigurationProvider.get(it).entityMetadata.name)
                    actionMap[nestedItem] = {
                        tabPane.tab(it)
                    }
                    childItem.children.add(nestedItem)
                }
                tableTreeItem.children.add(childItem)
            }

            val queryTreeItem = TreeItem("Запросы")
            queryTreeItem.isExpanded = true
            for ((groupName, query) in EntityConfigurationProvider.mapOfQueries) {
                val childItem = TreeItem(groupName)
                actionMap[childItem] = { tabPane.tab(query, isReadonly = true) }
                queryTreeItem.children.add(childItem)
            }

            rootTreeItem.children.add(tableTreeItem)
            rootTreeItem.children.add(queryTreeItem)
            treeview(rootTreeItem) {
                prefWidth = 320.0
                selectionModel.selectedItemProperty().addListener { _, oldValue, newValue ->
                    actionMap[newValue]?.invoke()
                }
                isShowRoot = false
            }

        }
        center {
            add(tabPane)
        }
    }
}

class MyApp : App(StartView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.isMaximized = true
        stage.minWidth = 1440.0
        stage.minHeight = 810.0
    }


}

fun main(args: Array<String>) {
    launch<MyApp>(args)
}