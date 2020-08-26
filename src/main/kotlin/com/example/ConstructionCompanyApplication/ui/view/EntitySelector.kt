package com.example.ConstructionCompanyApplication.ui.view

import com.example.ConstructionCompanyApplication.dto.AbstractEntity
import com.example.ConstructionCompanyApplication.ui.view.crud.SelectView
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import tornadofx.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

@Suppress("UNCHECKED_CAST")
class EntitySelector(property: KProperty1<*, ObjectProperty<AbstractEntity>>, var itemViewModel: ItemViewModel<*>) :
    HBox() {
    val textField = textfield {
        isEditable = false
        hgrow = Priority.ALWAYS
    }
    val selectedEntityProperty = SimpleObjectProperty<AbstractEntity>()
    val valueSelectedProperty = SimpleIntegerProperty()

    var selectedEntity: AbstractEntity?
    get() = selectedEntityProperty.value
    set(value) {
        selectedEntityProperty.value = value
        valueSelectedProperty += 1
    }

    init {
        val entityClass = property.returnType.arguments[0].type!!.classifier as KClass<out AbstractEntity>
        val selectView = SelectView(entityClass)

        button { text = "..." }.action {
            selectedEntity = selectView.select()
        }

        selectedEntityProperty.addListener { _, _, newValue ->
            textField.text = newValue?.toString().orEmpty()
        }
    }
}