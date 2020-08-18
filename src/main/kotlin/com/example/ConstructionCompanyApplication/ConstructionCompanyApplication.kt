package com.example.ConstructionCompanyApplication

import com.example.ConstructionCompanyApplication.ui.configuration.EntityConfigurationProvider
import com.example.ConstructionCompanyApplication.ui.view.MainTabPane
import com.example.ConstructionCompanyApplication.ui.view.crud.EditView
import javafx.scene.control.TreeItem
import tornadofx.*

class StartView : View("Строительная организация") {
    private val tabPane = MainTabPane()

    override val root = borderpane {
        left {
            val actionMap = mutableMapOf<TreeItem<*>, () -> Unit>()
            val rootTreeItem = TreeItem("Таблицы")
            rootTreeItem.isExpanded = true


            for ((groupName, entityList) in EntityConfigurationProvider.mapOfEntitiesByGroup) {
                val childItem = TreeItem(groupName)
                entityList.forEach {
                    val nestedItem = TreeItem(EntityConfigurationProvider.get(it).entityMetadata.name)
                    actionMap[nestedItem] = {
                        tabPane.tab(it)
                    }
                    childItem.children.add(nestedItem)
                }
                rootTreeItem.children.add(childItem)
            }

            treeview(rootTreeItem) {
                prefWidth = 320.0
                selectionModel.selectedItemProperty().addListener { _, oldValue, newValue ->
                    actionMap[newValue]?.invoke()
                }
            }

        }
        center {
            add(tabPane)
        }
    }
}

class MyApp : App(StartView::class)

fun main(args: Array<String>) {
    launch<MyApp>(args)
}