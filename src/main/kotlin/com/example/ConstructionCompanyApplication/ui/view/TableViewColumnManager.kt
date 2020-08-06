package com.example.ConstructionCompanyApplication.ui.view

import com.example.ConstructionCompanyApplication.dto.AbstractDto
import com.example.ConstructionCompanyApplication.dto.ColumnName
import com.example.ConstructionCompanyApplication.dto.SelectedProperty
import com.example.ConstructionCompanyApplication.dto.columnName
import javafx.beans.property.*
import javafx.beans.value.ObservableValue
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.util.Callback
import tornadofx.*
import java.time.LocalDate
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

@Suppress("UNCHECKED_CAST")
class TableViewColumnManager<DTO : AbstractDto>(tableView: TableView<DTO>, instanceClass: KClass<DTO>) {
    private val columnPropertiesMutable =
        mutableMapOf<KProperty1<DTO, ObservableValue<*>>, TableColumn<DTO, *>>()
    private val propertyHandlerMap = mutableMapOf<KProperty1<DTO, ObservableValue<*>>, KFunction<Unit>>()


    init {
        val item = instanceClass.createInstance()
        for (property in instanceClass.memberProperties.filter { it.findAnnotation<ColumnName>() != null }) {
            val returnClass = (property.returnType.classifier as KClass<*>).java
            columnPropertiesMutable[property as KProperty1<DTO, ObservableValue<*>>] =
                when {
                    IntegerProperty::class.java.isAssignableFrom(returnClass) -> {
                        property as KProperty1<DTO, SimpleIntegerProperty>
                        propertyHandlerMap[property] = ColumnHandler<DTO>::handleIntegerColumn
                        tableView.column(property.columnName, property)
                    }
                    LongProperty::class.java.isAssignableFrom(returnClass) -> {
                        property as KProperty1<DTO, SimpleLongProperty>
                        propertyHandlerMap[property] = ColumnHandler<DTO>::handleLongColumn
                        tableView.column(property.columnName, property)

                    }
                    DoubleProperty::class.java.isAssignableFrom(returnClass) -> {
                        property as KProperty1<DTO, SimpleDoubleProperty>
                        propertyHandlerMap[property] = ColumnHandler<DTO>::handleDoubleColumn
                        tableView.column(property.columnName, property)

                    }
                    FloatProperty::class.java.isAssignableFrom(returnClass) -> {
                        property as KProperty1<DTO, SimpleFloatProperty>
                        propertyHandlerMap[property] = ColumnHandler<DTO>::handleFloatColumn
                        tableView.column(property.columnName, property)

                    }
                    BooleanProperty::class.java.isAssignableFrom(returnClass) -> {
                        property as KProperty1<DTO, SimpleBooleanProperty>
                        propertyHandlerMap[property] = ColumnHandler<DTO>::handleBooleanColumn
                        tableView.column(property.columnName, property)

                    }
                    StringProperty::class.java.isAssignableFrom(returnClass) -> {
                        property as KProperty1<DTO, SimpleStringProperty>
                        propertyHandlerMap[property] = ColumnHandler<DTO>::handleStringColumn
                        tableView.column(property.columnName, property)
                    }
                    SelectedProperty::class.java.isAssignableFrom(returnClass) -> {
                        val selectedProperty = property.get(item) as SelectedProperty<*, *>
                        when {
                            (AbstractDto::class.java.isAssignableFrom(selectedProperty.sourceClass.java)) -> {
                                when (selectedProperty.valueClass) {
                                    String::class -> {
                                        property as KProperty1<DTO, SelectedProperty<DTO, String>>
                                        propertyHandlerMap[property] =
                                            ColumnHandler<DTO>::handleSelectedStringColumn
                                        tableView.column(property.columnName, property)

                                    }
                                    Long::class, Number::class -> {
                                        property as KProperty1<DTO, SelectedProperty<DTO, Long>>
                                        propertyHandlerMap[property] =
                                            ColumnHandler<DTO>::handleSelectedNumberColumn
                                        tableView.column(property.columnName, property)

                                    }
                                    else -> throw Exception("Unsupported SelectedProperty<, valueClass: ${selectedProperty.valueClass}>")
                                }

                            }
                            else -> throw Exception("Unsupported SelectedProperty")
                        }
                    }
                    //selector by value type of generic class
                    ObjectProperty::class.java.isAssignableFrom(returnClass) -> when ((property.get(item) as SimpleObjectProperty<*>).value) {
                        is LocalDate -> {
                            property as KProperty1<DTO, SimpleObjectProperty<LocalDate>>
                            propertyHandlerMap[property] = ColumnHandler<DTO>::handleLocalDateColumn
                            tableView.column(property.columnName, property)
                        }
                        is AbstractDto -> {
                            property as KProperty1<DTO, SimpleObjectProperty<AbstractDto>>
                            propertyHandlerMap[property] = ColumnHandler<DTO>::handleAbstractDtoColumn
                            tableView.column(property.columnName, property)
                        }
                        else -> throw Exception("Unsupported argument type of object property")
                    }
                    else -> throw Exception("Unsupported property")
                }
        }
    }

    fun handleProperties(handler: ColumnHandler<DTO>) {
        for ((property, column) in columnPropertiesMutable) {
            propertyHandlerMap[property]?.call(handler, property, column)
        }
    }

    fun <V> getColumn(property: KProperty1<DTO, ObservableValue<V>>) =
        columnPropertiesMutable[property] as TableColumn<DTO, V>



    interface ColumnHandler<T> {
        fun handleIntegerColumn(
            property: KProperty1<T, SimpleIntegerProperty>,
            column: TableColumn<T, Int>
        ) {
        }

        fun handleLongColumn(
            property: KProperty1<T, SimpleLongProperty>,
            column: TableColumn<T, Long>
        ) {
        }

        fun handleDoubleColumn(
            property: KProperty1<T, SimpleDoubleProperty>,
            column: TableColumn<T, Double>
        ) {
        }

        fun handleFloatColumn(
            property: KProperty1<T, SimpleFloatProperty>,
            column: TableColumn<T, Float>
        ) {
        }

        fun handleBooleanColumn(
            property: KProperty1<T, SimpleBooleanProperty>,
            column: TableColumn<T, Boolean>
        ) {
        }

        fun handleStringColumn(
            property: KProperty1<T, SimpleStringProperty>,
            column: TableColumn<T, String>
        ) {
        }

        fun handleLocalDateColumn(
            property: KProperty1<T, SimpleObjectProperty<LocalDate>>,
            column: TableColumn<T, LocalDate>
        ) {
        }

        fun handleAbstractDtoColumn(
            property: KProperty1<T, SimpleObjectProperty<AbstractDto>>,
            column: TableColumn<T, AbstractDto>
        ) {
        }

        fun handleSelectedStringColumn(
            property: KProperty1<T, SelectedProperty<AbstractDto, String>>,
            column: TableColumn<T, String>
        ) {
        }

        fun handleSelectedNumberColumn(
            property: KProperty1<T, SelectedProperty<AbstractDto, Number>>,
            column: TableColumn<T, Number>
        ) {
        }
    }


}

fun <S, T> TableView<S>.column(
    title: String,
    prop: KProperty1<S, ObservableValue<T>>,
    op: TableColumn<S, T>.() -> Unit = {}
): TableColumn<S, T> {
    val column = TableColumn<S, T>(title)
    column.cellValueFactory = Callback { prop.call(it.value) }
    addColumnInternal(column)
    return column.also(op)
}