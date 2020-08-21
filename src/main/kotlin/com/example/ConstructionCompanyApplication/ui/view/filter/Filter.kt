package com.example.ConstructionCompanyApplication.ui.view.filter

import com.example.ConstructionCompanyApplication.dto.BuildObject
import com.example.ConstructionCompanyApplication.dto.Customer
import com.example.ConstructionCompanyApplication.dto.Plot
import com.example.ConstructionCompanyApplication.dto.Prototype
import com.example.ConstructionCompanyApplication.service.RsqlFilterBuilder
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TextFormatter
import tornadofx.*
import java.time.LocalDate
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class BuildObjectFilter : Filter {
    val number = SimpleObjectProperty<String>()
    val startDate = SimpleObjectProperty<LocalDate>()
    val finishDate = SimpleObjectProperty<LocalDate>()
    val projectNumber = SimpleObjectProperty<String>()
    val plotNumber = SimpleObjectProperty<String>()
    val customerName = SimpleObjectProperty<String>()

    override fun buildRsql(): String {
        return RsqlFilterBuilder()
            .equal(BuildObject::id.name, number.value)
            .greaterOrEqual(BuildObject::startDate.name, startDate.value)
            .lessOrEqual(BuildObject::finishDate.name, finishDate.value)
            .equal(BuildObject::prototype.name + "." + Prototype::id.name, projectNumber.value)
            .equal(BuildObject::plot.name + "." + Plot::id.name, plotNumber.value)
            .substring(BuildObject::customer.name + "." + Customer::name.name, customerName.value)
            .build()

    }
}


interface Filter {
    fun buildRsql(): String
}

abstract class FilterView<T : Filter>(filterClass: KClass<T>) : View() {
    lateinit var onSearchRequest: (rsql: String) -> Unit

    protected val itemViewModel = run {
        val model = ItemViewModel<T>()
        model.item = filterClass.createInstance()
        model
    }

    protected val positiveNumberFilter: (TextFormatter.Change) -> Boolean = { change ->
        !change.isAdded || change.controlNewText.let {
            it.isLong() && it.toLong() > 0
        }
    }
    override val root = form {
        add(createFieldSet())
        button("Поиск").action {
            itemViewModel.commit()
            onSearchRequest(itemViewModel.item.buildRsql())
        }
        maxWidth = 400.0
    }

    abstract protected fun createFieldSet(): Fieldset

}

interface FilterViewFactory {
    fun create(): FilterView<*>
}

class BuildObjectFilterViewFactory : FilterViewFactory {
    override fun create(): FilterView<*> = object : FilterView<BuildObjectFilter>(BuildObjectFilter::class) {
        override fun createFieldSet(): Fieldset = with(Fieldset("Фильтр объекта")) {
            field("Номер объекта") {
                textfield(itemViewModel.bind(BuildObjectFilter::number)) {
                    filterInput(positiveNumberFilter)
                }
            }
            field("Начало строительсва") { datepicker(itemViewModel.bind(BuildObjectFilter::startDate)) }
            field("Конец строительсва") { datepicker(itemViewModel.bind(BuildObjectFilter::finishDate)) }
            field("Номер проекта") {
                textfield(itemViewModel.bind(BuildObjectFilter::projectNumber)) {
                    filterInput(positiveNumberFilter)
                }
            }
            field("Номер участка") {
                textfield(itemViewModel.bind(BuildObjectFilter::plotNumber)) {
                    filterInput(positiveNumberFilter)
                }
            }
            field("Заказчик") { textfield(itemViewModel.bind(BuildObjectFilter::customerName)) }
            return@with this
        }
    }
}
