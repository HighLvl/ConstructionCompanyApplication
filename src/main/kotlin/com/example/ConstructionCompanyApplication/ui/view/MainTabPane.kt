package com.example.ConstructionCompanyApplication.ui.view

import com.example.ConstructionCompanyApplication.dto.AbstractEntity
import com.example.ConstructionCompanyApplication.ui.view.crud.EditView
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import tornadofx.*
import kotlin.reflect.KClass

class MainTabPane: TabPane() {
    fun tab(entityClass: KClass<out AbstractEntity>, dataSource: String? = null, info: String? = null, isReadonly: Boolean = false) {
        val view = EditView(entityClass)
        if (dataSource != null) {
            view.setDataSource(dataSource)
        }
        val prefix = if(info != null) "$info: " else ""
        selectionModel.select(tab(prefix + view.title, view.root))
        if(isReadonly) {
            view.setReadonlyMode()
        }
    }
}