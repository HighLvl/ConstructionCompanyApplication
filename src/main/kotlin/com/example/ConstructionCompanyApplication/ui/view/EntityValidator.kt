package com.example.ConstructionCompanyApplication.ui.view

import com.example.ConstructionCompanyApplication.dto.AbstractEntity
import javafx.beans.property.ObjectProperty
import javafx.scene.control.DatePicker
import javafx.scene.control.TableColumn
import tornadofx.*
import java.time.LocalDate
import kotlin.reflect.KProperty1

@Suppress("UNCHECKED_CAST")
class EntityValidator<T: AbstractEntity> {
    private val validatorMap = mutableMapOf<KProperty1<T, ObjectProperty<*>>, Pair<(Any?, T) -> Boolean, String>>()
    lateinit var item: T

    fun <S: Any> setValidator(property: KProperty1<T, ObjectProperty<*>>, errorMessage: String = "", validator: (newValue: S?, item: T) -> Boolean ) {
        validatorMap[property] = validator as (Any?, T) -> Boolean to errorMessage
    }

    fun isValid(property: KProperty1<T, ObjectProperty<*>>, newValue: Any?): Boolean {
        return validatorMap[property]?.first?.invoke(newValue, item) ?: true
    }

    fun getErrorMessage(property: KProperty1<T, ObjectProperty<*>>): String {
        return validatorMap[property]?.second ?: ""
    }
}