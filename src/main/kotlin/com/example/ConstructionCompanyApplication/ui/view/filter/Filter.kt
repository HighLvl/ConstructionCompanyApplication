package com.example.ConstructionCompanyApplication.ui.view.filter

import com.example.ConstructionCompanyApplication.dto.*
import com.example.ConstructionCompanyApplication.dto.query.MaterialConsumptionReport
import com.example.ConstructionCompanyApplication.dto.query.Report
import com.example.ConstructionCompanyApplication.service.RsqlFilterBuilder
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TextFormatter
import tornadofx.*
import java.time.LocalDate
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance

fun toFilterKey(vararg properties: KProperty1<*, *>): String {
    return properties.map { it.name }.toList().joinToString(".")
}

class BuildObjectFilter : Filter {
    val number = SimpleObjectProperty<String>()
    val startDate = SimpleObjectProperty<LocalDate>()
    val finishDate = SimpleObjectProperty<LocalDate>()
    val projectNumber = SimpleObjectProperty<String>()
    val plotNumber = SimpleObjectProperty<String>()
    val managementNumber = SimpleObjectProperty<String>()
    val customerName = SimpleObjectProperty<String>()

    override fun buildRsql(): String {
        return RsqlFilterBuilder()
            .equal(BuildObject::id.name, number.value)
            .greaterOrEqual(BuildObject::startDate.name, startDate.value)
            .lessOrEqual(BuildObject::finishDate.name, finishDate.value)
            .equal(toFilterKey(BuildObject::prototype, Prototype::id), projectNumber.value)
            .equal(toFilterKey(BuildObject::plot, Plot::id), plotNumber.value)
            .equal(toFilterKey(BuildObject::plot, Plot::management, Management::id), managementNumber.value)
            .substring(toFilterKey(BuildObject::customer, Customer::name), customerName.value)
            .build()

    }
}

class ReportFilter : Filter {
    val buildObjectNumber = SimpleObjectProperty<String>()
    val brigadeName = SimpleObjectProperty<String>()
    val managementNumber = SimpleObjectProperty<String>()
    val plotNumber = SimpleObjectProperty<String>()
    val workTypeName = SimpleObjectProperty<String>()
    val startDate = SimpleObjectProperty<LocalDate>()
    val finishDate = SimpleObjectProperty<LocalDate>()
    val isOverDeadline = SimpleObjectProperty<Boolean>()

    override fun buildRsql(): String {
        return RsqlFilterBuilder()
            .equal(toFilterKey(Report::buildObject, BuildObject::id), buildObjectNumber.value)
            .substring(toFilterKey(Report::brigade, Brigade::name), brigadeName.value)
            .equal(
                toFilterKey(Report::buildObject, BuildObject::plot, Plot::management, Management::id),
                managementNumber.value
            )
            .equal(toFilterKey(Report::buildObject, BuildObject::plot, Plot::id), plotNumber.value)
            .substring(toFilterKey(Report::workType, WorkType::name), workTypeName.value)
            .greaterOrEqual(Report::startDate.name, startDate.value)
            .lessOrEqual(Report::finishDate.name, finishDate.value)
            .greater(Report::timeOverrun.name, if (isOverDeadline.value == false) null else 0)
            .build()
    }
}

class MaterialConsumptionReportFilter : Filter {
    val managementNumber = SimpleObjectProperty<String>()
    val plotNumber = SimpleObjectProperty<String>()
    val isOverEstimate = SimpleObjectProperty<Boolean>()

    override fun buildRsql(): String {
        return RsqlFilterBuilder()
            .greater(MaterialConsumptionReport::matConsOverrun.name, if (isOverEstimate.value == false) null else 0)
            .equal(
                toFilterKey(MaterialConsumptionReport::report, Report::buildObject, BuildObject::plot, Plot::management, Management::id),
                managementNumber.value
            )
            .equal(toFilterKey(MaterialConsumptionReport::report, Report::buildObject, BuildObject::plot, Plot::id), plotNumber.value)
            .build()

    }
}


interface Filter {
    fun buildRsql(): String
}

open class FilterView<T : Filter>(filterName: String, filterClass: KClass<T>) : View() {
    lateinit var onSearchRequest: (rsql: String) -> Unit

    val itemViewModel = run {
        val model = ItemViewModel<T>()
        model.item = filterClass.createInstance()
        model
    }

    val fieldSet = Fieldset(filterName)

    override val root = form {
        add(fieldSet)
        button("Поиск").action {
            itemViewModel.commit()
            onSearchRequest(itemViewModel.item.buildRsql())
        }
        maxWidth = 400.0
    }
}

class FilterViewBuilder<T : Filter>(filterName: String, filterClass: KClass<T>) {
    private val filterView = FilterView(filterName, filterClass)
    private val fieldSet = filterView.fieldSet
    private val itemViewModel = filterView.itemViewModel
    private val positiveNumberFilter: (TextFormatter.Change) -> Boolean = { change ->
        !change.isAdded || change.controlNewText.let {
            it.isLong() && it.toLong() > 0
        }
    }

    fun addPositiveNumberField(
        name: String,
        property: KProperty1<T, SimpleObjectProperty<String>>
    ): FilterViewBuilder<T> {
        fieldSet.field(name) {
            textfield(itemViewModel.bind(property)) {
                filterInput(positiveNumberFilter)
            }
        }
        return this
    }

    fun addDatePickerField(
        name: String,
        property: KProperty1<T, SimpleObjectProperty<LocalDate>>
    ): FilterViewBuilder<T> {
        fieldSet.field(name) { datepicker(itemViewModel.bind(property)) }
        return this
    }

    fun addTextField(name: String, property: KProperty1<T, SimpleObjectProperty<String>>): FilterViewBuilder<T> {
        fieldSet.field(name) { textfield(itemViewModel.bind(property)) }
        return this
    }

    fun addCheckBoxField(name: String, property: KProperty1<T, SimpleObjectProperty<Boolean>>): FilterViewBuilder<T> {
        fieldSet.field(name) { checkbox(property = itemViewModel.bind(property)) }
        return this
    }

    fun build() = filterView


}

interface FilterViewFactory {
    fun create(): FilterView<*>
}

class BuildObjectFilterViewFactory : FilterViewFactory {
    override fun create() = FilterViewBuilder("Фильтр объекта", BuildObjectFilter::class)
        .addPositiveNumberField("Номер объекта", BuildObjectFilter::number)
        .addDatePickerField("Начало строительсва", BuildObjectFilter::startDate)
        .addDatePickerField("Конец строительсва", BuildObjectFilter::finishDate)
        .addPositiveNumberField("Номер проекта", BuildObjectFilter::projectNumber)
        .addPositiveNumberField("Номер участка", BuildObjectFilter::plotNumber)
        .addPositiveNumberField("Номер управления", BuildObjectFilter::managementNumber)
        .addTextField("Заказчик", BuildObjectFilter::customerName)
        .build()
}

class ReportFilterViewFactory : FilterViewFactory {
    override fun create(): FilterView<*> = FilterViewBuilder("Фильтр", ReportFilter::class)
        .addPositiveNumberField("Номер объекта", ReportFilter::buildObjectNumber)
        .addTextField("Бригада", ReportFilter::brigadeName)
        .addPositiveNumberField("Номер управления", ReportFilter::managementNumber)
        .addPositiveNumberField("Номер участка", ReportFilter::plotNumber)
        .addTextField("Вид работ", ReportFilter::workTypeName)
        .addDatePickerField("Начало работ", ReportFilter::startDate)
        .addDatePickerField("Конец работ", ReportFilter::finishDate)
        .addCheckBoxField("Превышение сроков", ReportFilter::isOverDeadline)
        .build()

}

class MaterialConsumptionReportFilterViewFactory: FilterViewFactory {
    override fun create(): FilterView<*> = FilterViewBuilder("Фильтр", MaterialConsumptionReportFilter::class)
        .addPositiveNumberField("Номер управления", MaterialConsumptionReportFilter::managementNumber)
        .addPositiveNumberField("Номер участка", MaterialConsumptionReportFilter::plotNumber)
        .addCheckBoxField("Перерасход", MaterialConsumptionReportFilter::isOverEstimate)
        .build()

}
