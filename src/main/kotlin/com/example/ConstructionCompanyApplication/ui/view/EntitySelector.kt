package com.example.ConstructionCompanyApplication.ui.view

import com.example.ConstructionCompanyApplication.dto.AbstractEntity
import com.example.ConstructionCompanyApplication.ui.view.crud.SelectView
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import tornadofx.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

@Suppress("UNCHECKED_CAST")
class EntitySelector(property: KProperty1<*, ObjectProperty<AbstractEntity>>, var itemViewModel: ItemViewModel<*>) : HBox() {
    var onSelectEntityListener: ((AbstractEntity?) -> Unit)? = null

    val textField = textfield {
        isEditable = false
        hgrow = Priority.ALWAYS
    }
    var selectedEntity: AbstractEntity? = null
    val valueSelectedProperty = SimpleIntegerProperty()

    init {
        itemViewModel.itemProperty.addListener { _, _, _ -> textField.text = "" }
        val entityClass = property.returnType.arguments[0].type!!.classifier as KClass<out AbstractEntity>
        val selectView = SelectView(entityClass)

        button { text = "..." }.action {
            selectedEntity = selectView.select()
            onSelectEntityListener?.invoke(selectedEntity)
            valueSelectedProperty += 1
            textField.text = selectedEntity?.toString()
        }
    }
}