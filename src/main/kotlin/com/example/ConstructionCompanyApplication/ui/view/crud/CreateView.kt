package com.example.ConstructionCompanyApplication.ui.view.crud

import com.example.ConstructionCompanyApplication.controller.CommonController
import com.example.ConstructionCompanyApplication.dto.AbstractEntity
import com.example.ConstructionCompanyApplication.ui.configuration.EntityConfigurationProvider
import com.example.ConstructionCompanyApplication.ui.view.*
import javafx.beans.property.ObjectProperty
import javafx.collections.ListChangeListener
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import tornadofx.*
import tornadofx.converter.UnitConverter
import java.time.LocalDate
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance

@Suppress("UNCHECKED_CAST")
class CreateView<T : AbstractEntity>(private val entityClass: KClass<T>) :
    View(EntityConfigurationProvider.get(entityClass).entityMetadata.name) {
    override val root: StackPane by fxml("/view/CreateView.fxml")
    private val saveButton: Button by fxid()
    private val totalElementsLabel: Label by fxid()
    private val fieldSetVBox: VBox by fxid()
    private val addElementButton: Button by fxid()
    private val itemViewModel = ItemViewModel<T>()
    private val tableView = EntityConfigurationProvider.get(entityClass).tableViewFactory.create() as EntityTableView<T>
    private val tableViewAnchorPane: AnchorPane by fxid()

    private val toDeleteList = observableListOf<T>()
    private val itemList = observableListOf<T>()

    private val controller = CommonController(entityClass)

    init {
        itemViewModel.itemProperty.addListener { _, _, newValue ->
            tableView.validator.item = newValue
        }

        initAddElementButton()
        initDeleteColumn()
        initTableView()
        initEditFieldSet()
        initSaveButton()

        itemViewModel.propertyMap.keys.forEach {
            it.addListener { _, _, _ ->
                itemViewModel.commit(it)
            }
        }
    }

    private fun initDeleteColumn() {
        tableView.addColumnInternal(
            DeleteTableColumn(
                toDeleteList
            )
        )
        toDeleteList.addListener(ListChangeListener {
            while (it.next()) {
                if (it.wasAdded()) {
                    itemList.removeAll(it.addedSubList)
                    toDeleteList.removeAll(it.addedSubList)
                }
            }
        })
    }

    private fun initTableView() {
        tableView.items = itemList
        tableView.columnResizePolicy = SmartResize.POLICY

        tableViewAnchorPane.add(tableView)
        AnchorPane.setBottomAnchor(tableView, 44.0)
        AnchorPane.setLeftAnchor(tableView, 0.0)
        AnchorPane.setRightAnchor(tableView, 0.0)
        AnchorPane.setTopAnchor(tableView, 0.0)

        totalElementsLabel.text = "0"
        itemList.addListener(ListChangeListener { totalElementsLabel.text = itemList.size.toString() })
    }

    private fun initAddElementButton() {
        itemViewModel.item = entityClass.createInstance()
        addElementButton.action {
            itemViewModel.commit()
            itemList.add(itemViewModel.item)
            itemViewModel.item = entityClass.createInstance()
        }
        addElementButton.enableWhen(itemViewModel.valid)
    }

    private fun initEditFieldSet() {
        val fieldset = fieldset()
        fieldSetVBox.add(form { add(fieldset) })

        tableView.handleProperties(object :
            EntityTableView.PropertyHandler<T> {
            override fun handleStringProperty(name: String, property: KProperty1<T, ObjectProperty<String>>) {
                fieldset.add(field(name))
                fieldset.add(textfield(itemViewModel.bind(property)) {
                    filterInput {
                        tableView.validator.isValid(
                            property,
                            it.controlNewText
                        )
                    }
                })
            }

            override fun handleIntegerProperty(name: String, property: KProperty1<T, ObjectProperty<Int>>) {
                fieldset.add(field(name))
                fieldset.add(textfield(itemViewModel.bind(property)) {
                    filterNumberInput(tableView.validator, property)
                })
            }

            override fun handleLongProperty(name: String, property: KProperty1<T, ObjectProperty<Long>>) {
                fieldset.add(field(name))
                val textField = textfield { filterNumberInput(tableView.validator, property) }
                textField.textProperty().bindBidirectional(itemViewModel.bind(property), UnitConverter())
                fieldset.add(textField)
            }

            override fun handleEntityProperty(name: String, property: KProperty1<T, ObjectProperty<AbstractEntity>>) {
                val entitySelector = EntitySelector(property, itemViewModel)
                entitySelector.onSelectEntityListener = { entity ->
                    property.get(itemViewModel.item).set(entity)
                }
                entitySelector.addValidator(tableView.validator, property)
                fieldset.add(field(name))
                fieldset.add(entitySelector)
            }

            override fun handleBooleanProperty(name: String, property: KProperty1<T, ObjectProperty<Boolean>>) {
                fieldset.add(field(name))
                val checkBox = checkbox()
                checkBox.bind(itemViewModel.bind(property))
                fieldset.add(checkBox)
            }

            override fun handleLocalDateProperty(name: String, property: KProperty1<T, ObjectProperty<LocalDate>>) {
                fieldset.add(field(name))
                val datePicker = datepicker(itemViewModel.bind(property))
                datePicker.addValidator(tableView.validator, property)
                fieldset.add(datePicker)
            }
        })
    }


    private fun reset() {
        itemList.clear()
    }

    private fun initSaveButton() {
        saveButton.enableWhen(itemList.sizeProperty.greaterThan(0))
        saveButton.action {
            controller.saveAll(emptyList(), itemList)
                .setProgressIndicator(root)
                .subscribe(
                {
                    reset()
                    alert(Alert.AlertType.INFORMATION, "Записи успешно добавлены")
                },
                {
                    alert(Alert.AlertType.ERROR, "Ошибка", it.message)
                }
            )

        }
    }
}

