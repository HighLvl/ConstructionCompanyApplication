package com.example.ConstructionCompanyApplication.ui.view

import com.example.ConstructionCompanyApplication.dto.AbstractEntity
import com.example.ConstructionCompanyApplication.ui.view.crud.CreateView
import javafx.beans.property.ObjectProperty
import javafx.scene.control.DatePicker
import javafx.scene.control.TableCell
import javafx.scene.control.TextInputControl
import tornadofx.*
import java.time.LocalDate
import kotlin.error
import kotlin.reflect.KProperty1

fun <T: AbstractEntity>DatePicker.addValidator(validator: EntityValidator<T>, property: KProperty1<T, ObjectProperty<LocalDate>>) {
    validator {
        if (!validator.isValid(property, value)) error(
            validator.getErrorMessage(
                property
            )
        ) else null
    }
}

fun <T: AbstractEntity>EntitySelector.addValidator(validator: EntityValidator<T>, property: KProperty1<T, ObjectProperty<AbstractEntity>>) {
    itemViewModel.addValidator(
        textField,
        valueSelectedProperty
    ) {
        if (!validator.isValid(property, selectedEntity)) {
            error(validator.getErrorMessage(property))
        } else {
            null
        }
    }
}

fun <T: AbstractEntity>TextInputControl.filterNumberInput(validator: EntityValidator<T>, property: KProperty1<T, ObjectProperty<out Number>>) {
    filterInput { change ->
        change.controlNewText.let {
            if (!it.isLong()) return@let false
            val value = it.toLong()
            validator.isValid(
                property,
                value
            )
        }
    }
}