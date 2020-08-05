package com.example.ConstructionCompanyApplication.ui.viewmodel

import com.example.ConstructionCompanyApplication.dto.*
import javafx.beans.property.*
import javafx.beans.value.ObservableValue
import tornadofx.*
import java.lang.reflect.ParameterizedType
import java.time.LocalDate
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

@Suppress("UNCHECKED_CAST")
abstract class AbstractItemViewModel<T : AbstractDto>() : ItemViewModel<T>() {

    val columnProperties: Map<KProperty1<T, ObservableValue<*>>, ObservableValue<*>>
        get() = columnPropertiesMutable

    private val columnPropertiesMutable =
        mutableMapOf<KProperty1<T, ObservableValue<*>>, ObservableValue<*>>()
    private val propertyHandlerMap = mutableMapOf<KProperty1<T, ObservableValue<*>>, KFunction<Unit>>()


    private val instanceClass: KClass<T> = ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>).kotlin

    init {
        item = instanceClass.createInstance()

        for (property in instanceClass.memberProperties.filter { it.findAnnotation<ColumnName>() != null }.reversed()) {
            val returnClass = (property.returnType.classifier as KClass<*>).java
            columnPropertiesMutable[property as KProperty1<T, ObservableValue<*>>] =
                when {
                    IntegerProperty::class.java.isAssignableFrom(returnClass) -> {
                        propertyHandlerMap[property] = PropertyHandler<T>::handleIntegerProperty
                        bind(property as KProperty1<T, SimpleIntegerProperty>)
                    }
                    LongProperty::class.java.isAssignableFrom(returnClass) -> {
                        propertyHandlerMap[property] = PropertyHandler<T>::handleLongProperty
                        bind(property as KProperty1<T, SimpleLongProperty>)
                    }
                    DoubleProperty::class.java.isAssignableFrom(returnClass) -> {
                        propertyHandlerMap[property] = PropertyHandler<T>::handleDoubleProperty
                        bind(property as KProperty1<T, SimpleDoubleProperty>)
                    }
                    FloatProperty::class.java.isAssignableFrom(returnClass) -> {
                        propertyHandlerMap[property] = PropertyHandler<T>::handleFloatProperty
                        bind(property as KProperty1<T, SimpleFloatProperty>)
                    }
                    BooleanProperty::class.java.isAssignableFrom(returnClass) -> {
                        propertyHandlerMap[property] = PropertyHandler<T>::handleBooleanProperty
                        bind(property as KProperty1<T, SimpleBooleanProperty>)
                    }
                    StringProperty::class.java.isAssignableFrom(returnClass) -> {
                        propertyHandlerMap[property] = PropertyHandler<T>::handleStringProperty
                        bind(property as KProperty1<T, SimpleStringProperty>)
                    }
                    SelectedProperty::class.java.isAssignableFrom(returnClass) -> {
                        val selectedProperty = property.get(item) as SelectedProperty<*, *>
                        when {
                            (AbstractDto::class.java.isAssignableFrom(selectedProperty.sourceClass.java) && selectedProperty.valueClass == String::class) -> {
                                propertyHandlerMap[property] = PropertyHandler<T>::handleSelectedStringProperty
                                bind(property as KProperty1<T, SimpleStringProperty>)
                            }
                            else -> throw Exception("Unsupported SelectedProperty")
                        }
                    }
                    //selector by value type of generic class
                    ObjectProperty::class.java.isAssignableFrom(returnClass) -> when ((property.get(item) as SimpleObjectProperty<*>).value) {
                        is LocalDate -> {
                            propertyHandlerMap[property] = PropertyHandler<T>::handleLocalDateProperty
                            bind(property as KProperty1<T, SimpleObjectProperty<LocalDate>>)
                        }
                        is AbstractDto -> {
                            propertyHandlerMap[property] = PropertyHandler<T>::handleAbstractDtoProperty
                            bind(property as KProperty1<T, SimpleObjectProperty<AbstractDto>>)
                        }
                        else -> throw Exception("Unsupported argument type of object property")
                    }
                    else -> throw Exception("Unsupported property")
                }
        }
    }

    fun handleProperties(handler: PropertyHandler<T>) {
        for ((property, observableValue) in columnPropertiesMutable) {
            propertyHandlerMap[property]?.call(handler, property, observableValue)
        }
    }

    interface PropertyHandler<T> {
        fun handleIntegerProperty(
            property: KProperty1<T, SimpleIntegerProperty>,
            observableValue: ObservableValue<Int>
        ) {
        }

        fun handleLongProperty(
            property: KProperty1<T, SimpleLongProperty>,
            observableValue: ObservableValue<Long>
        ) {
        }

        fun handleDoubleProperty(
            property: KProperty1<T, SimpleDoubleProperty>,
            observableValue: ObservableValue<Double>
        ) {
        }

        fun handleFloatProperty(
            property: KProperty1<T, SimpleFloatProperty>,
            observableValue: ObservableValue<Float>
        ) {
        }

        fun handleBooleanProperty(
            property: KProperty1<T, SimpleBooleanProperty>,
            observableValue: ObservableValue<Boolean>
        ) {
        }

        fun handleStringProperty(
            property: KProperty1<T, SimpleStringProperty>,
            observableValue: ObservableValue<String>
        ) {
        }

        fun handleLocalDateProperty(
            property: KProperty1<T, SimpleObjectProperty<LocalDate>>,
            observableValue: ObservableValue<LocalDate>
        ) {
        }

        fun handleAbstractDtoProperty(
            property: KProperty1<T, SimpleObjectProperty<AbstractDto>>,
            observableValue: ObservableValue<AbstractDto>
        ) {
        }

        fun handleSelectedStringProperty(
            property: KProperty1<T, SelectedProperty<AbstractDto, String>>,
            observableValue: ObservableValue<String>
        ) {
        }
    }


}

class BrigadeMemberViewModel : AbstractItemViewModel<BrigadeMember>()


class BrigadeViewModel : AbstractItemViewModel<Brigade>()


class BuildObjectViewModel : AbstractItemViewModel<BuildObject>()


class CustomerViewModel : AbstractItemViewModel<Customer>()


class EstimateViewModel : AbstractItemViewModel<Estimate>()


class MachineryViewModel : AbstractItemViewModel<Machinery>()


class MachineryModelViewModel : AbstractItemViewModel<MachineryModel>()


class MachineryTypeViewModel : AbstractItemViewModel<MachineryType>()


class ManagementViewModel : AbstractItemViewModel<Management>()


class MaterialViewModel : AbstractItemViewModel<Material>()


class MaterialConsumptionViewModel : AbstractItemViewModel<MaterialConsumption>()


class ObjectBrigadeViewModel : AbstractItemViewModel<ObjectBrigade>()


class ObjectMachineryViewModel : AbstractItemViewModel<ObjectMachinery>()


class PlotViewModel : AbstractItemViewModel<Plot>()


class PrototypeViewModel : AbstractItemViewModel<Prototype>()


class PrototypeTypeViewModel : AbstractItemViewModel<PrototypeType>()


class StaffViewModel : AbstractItemViewModel<Staff>()


class TitleViewModel : AbstractItemViewModel<Title>()


class TitleCategoryViewModel : AbstractItemViewModel<TitleCategory>()


class WorkScheduleViewModel : AbstractItemViewModel<WorkSchedule>()


class WorkTypeViewModel : AbstractItemViewModel<WorkType>()


