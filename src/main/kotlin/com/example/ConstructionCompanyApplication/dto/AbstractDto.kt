package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableValue
import tornadofx.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation

abstract class AbstractDto(open var id: Long? = null) {

    override fun equals(other: Any?): Boolean {
        if (this.javaClass != other?.javaClass) return false
        if (this === other) return true
        if (other !is AbstractDto) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}

@Target(AnnotationTarget.PROPERTY)
annotation class ColumnName (
    val value : String
)

val KProperty1<*, ObservableValue<*>>.columnName get() = findAnnotation<ColumnName>()?.value ?: ""

fun <T, N> ObservableValue<T>.extractNested(nested: (T) -> ObservableValue<N>): ObservableValue<N>? = value?.let(nested)
inline fun <reified T: Any, reified N: Any> ObservableValue<T>.select(noinline nested: (T) -> ObservableValue<N>): SelectedProperty<T, N> {
    var currentNested: ObservableValue<N>? = extractNested(nested)

    return object : SelectedProperty<T, N>(T::class, N::class) {
        val changeListener = ChangeListener<Any?> { _, _, _ ->
            invalidated()
            fireValueChangedEvent()
        }

        init {
            currentNested?.addListener(changeListener)
            this@select.addListener(changeListener)
        }

        override fun invalidated() {
            currentNested?.removeListener(changeListener)
            currentNested = extractNested(nested)
            currentNested?.addListener(changeListener)
        }

        override fun get() = currentNested?.value

        override fun set(v: N?) {
            (currentNested as? WritableValue<N>)?.value = v
            super.set(v)
        }

    }

}

abstract class SelectedProperty<T : Any, N: Any>(val sourceClass: KClass<T>, val valueClass: KClass<N>) : SimpleObjectProperty<N>()