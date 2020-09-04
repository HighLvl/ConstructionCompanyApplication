package com.example.ConstructionCompanyApplication.ui.view

import com.example.ConstructionCompanyApplication.dto.AbstractEntity
import com.example.ConstructionCompanyApplication.dto.Estimate
import com.example.ConstructionCompanyApplication.ui.configuration.EntityConfigurationProvider
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.layout.HBox
import javafx.util.StringConverter
import org.springframework.data.domain.Sort
import tornadofx.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/*Сортировка*/
class SortBox(private val tableView: EntityTableView<*>) : HBox() {
    private val label = label("Сортировать:")
    private val selectColumnComboBox = combobox(SimpleObjectProperty<TableColumn<*, *>>())
    private val directionComboBox = combobox(SimpleStringProperty(), listOf(ASCENDING, DESCENDING))

    private val direction: Sort.Direction
        get() = when (directionComboBox.value) {
            ASCENDING -> Sort.Direction.ASC
            DESCENDING -> Sort.Direction.DESC
            else -> Sort.DEFAULT_DIRECTION
        }

    val selectColumnProperty: ObjectProperty<TableColumn<*, *>> = selectColumnComboBox.valueProperty()
    val directionProperty: ObjectProperty<String> = directionComboBox.valueProperty()

    /*Направление сортировки и сортируемые столбцы. Сортировка ссылаемых сущностей происходит по вложенным полям*/
    val sort: Sort
        get() {
            val sortProperties = getAllSortProperties() ?: return Sort.unsorted()
            return Sort.by(direction, *sortProperties.toTypedArray())
        }

    init {
        selectColumnComboBox.converter = object : StringConverter<TableColumn<*, *>>() {
            override fun toString(obj: TableColumn<*, *>?): String {
                return obj?.text.orEmpty()
            }

            override fun fromString(string: String?): TableColumn<*, *> {
                TODO("Not yet implemented")
            }

        }
        tableView.propertyColumns.forEach {
            selectColumnComboBox.items.add(it)
        }
        setMargin(label, Insets(10.0, 0.0, 10.0, 10.0))
        spacing = 30.0
    }

    private fun getAllSortProperties(
    ): List<String>? {
        selectColumnComboBox.value ?: return null
        val list = mutableListOf<String>()
        addAllSortProperties("", tableView.getPropertyBy(selectColumnComboBox.value), list)
        return list
    }

    private fun addAllSortProperties(
        sourcePropertyName: String,
        property: KProperty1<*, *>,
        list: MutableList<String>
    ) {
        val sortClass = property.returnType.arguments[0].type!!.classifier as KClass<*>
        if (!AbstractEntity::class.java.isAssignableFrom(sortClass.java)) {
            list.add(sourcePropertyName + property.name)
            return
        }
        val sortableProperties = EntityConfigurationProvider.get(sortClass).entityMetadata.sortableProperties
        sortableProperties?.forEach {
            addAllSortProperties("$sourcePropertyName${property.name}.", it, list)
        } ?: list.add(property.name)
    }

    companion object {
        const val ASCENDING = "По возрастанию"
        const val DESCENDING = "По убыванию"
    }
}