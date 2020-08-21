package com.example.ConstructionCompanyApplication.ui.view.crud

import com.example.ConstructionCompanyApplication.ui.configuration.EntityConfigurationProvider
import com.example.ConstructionCompanyApplication.dto.AbstractEntity
import com.example.ConstructionCompanyApplication.controller.CommonController
import com.example.ConstructionCompanyApplication.ui.view.DeleteTableColumn
import com.example.ConstructionCompanyApplication.ui.view.EntityTableView
import javafx.beans.property.ObjectProperty
import javafx.collections.ListChangeListener
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import tornadofx.*
import tornadofx.converter.UnitConverter
import java.time.LocalDate
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance

@Suppress("UNCHECKED_CAST")
class CreateView<T : AbstractEntity>(private val entityClass: KClass<T>) :
    View(EntityConfigurationProvider.get(entityClass).entityMetadata.name) {
    override val root: BorderPane by fxml("/view/CreateTable.fxml")
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

    private val stage = Stage()

    init {
        initDeleteColumn()
        initTableView()
        initEditFieldSet()
        initAddElementButton()
        initSaveButton()
        initStage()
    }

    fun show() = stage.showAndWait()


    private fun initStage() {
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.scene = Scene(root)
        stage.title = title
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
    }

    private fun initEditFieldSet() {
        val fieldset = fieldset()
        fieldSetVBox.add(form { add(fieldset) })

        tableView.handleProperties(object :
            EntityTableView.EditablePropertyHandler<T> {
            override fun handleStringProperty(name: String, property: KProperty1<T, ObjectProperty<String>>) {
                fieldset.add(field(name))
                fieldset.add(textfield(itemViewModel.bind(property)))
            }

            override fun handleIntegerProperty(name: String, property: KProperty1<T, ObjectProperty<Int>>) {
                fieldset.add(field(name))
                fieldset.add(textfield(itemViewModel.bind(property)))
            }

            override fun handleLongProperty(name: String, property: KProperty1<T, ObjectProperty<Long>>) {
                fieldset.add(field(name))
                val textField = textfield()
                textField.textProperty().bindBidirectional(itemViewModel.bind(property), UnitConverter())
                fieldset.add(textField)
            }

            override fun handleEntityProperty(name: String, property: KProperty1<T, ObjectProperty<AbstractEntity>>) {
                fieldset.add(field(name))
                fieldset.add(buildEntitySelector(property))
            }

            override fun handleBooleanProperty(name: String, property: KProperty1<T, ObjectProperty<Boolean>>) {
                fieldset.add(field(name))
                val checkBox = checkbox()
                checkBox.bind(itemViewModel.bind(property))
                fieldset.add(checkBox)
            }

            override fun handleLocalDateProperty(name: String, property: KProperty1<T, ObjectProperty<LocalDate>>) {
                fieldset.add(field(name))
                fieldset.add(datepicker(itemViewModel.bind(property)))
            }
        })
    }

    private fun buildEntitySelector(property: KProperty1<T, ObjectProperty<AbstractEntity>>) = hbox {
        val textField = textfield {
            isEditable = false
            hgrow = Priority.ALWAYS
        }
        itemViewModel.itemProperty.addListener { _, _, _ -> textField.text = "" }

        val entityClass = property.returnType.arguments[0].type!!.classifier as KClass<T>
        val selectView =
            SelectView(entityClass)

        button { text = "..." }.action {
            val entity = selectView.select()
            property.get(itemViewModel.item).set(entity)
            textField.text = entity.toString()
        }
    }

    private fun reset() {
        itemList.clear()
    }

    private fun initSaveButton() {
        saveButton.enableWhen(itemList.sizeProperty.greaterThan(0))
        saveButton.action {
            controller.saveAll(itemList)
            reset()
            stage.close()
        }
    }
}
