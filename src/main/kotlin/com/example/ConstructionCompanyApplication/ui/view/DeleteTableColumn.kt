package com.example.ConstructionCompanyApplication.ui.view

import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.paint.Color
import tornadofx.*

class DeleteTableColumn<T>(private val toDeleteList: ObservableList<T>) : TableColumn<T, T>() {
    init {
        setCellValueFactory {
            ReadOnlyObjectWrapper(it.value)
        }
        minWidth = 100.0
        cellFormat {

            val deleteButton = Button("X")
            deleteButton.prefWidth = 80.0
            deleteButton.style { textFill = Color.DARKRED }
            deleteButton.action {
                onClickDeleteButton(tableRow.item)
            }
            tableRow?.style {
                if (tableRow.item in toDeleteList) {
                    backgroundColor += Color.GRAY
                } else {
                    backgroundColor.elements.remove(Color.GRAY)
                }
            }

            graphic = deleteButton
            minHeight = 50.0
            style { alignment = Pos.CENTER_RIGHT }
        }
        isSortable = false

        toDeleteList.addListener(ListChangeListener {
            tableView.refresh()
        })
    }

    private fun onClickDeleteButton(item: T) {
        if (item in toDeleteList) {
            toDeleteList.remove(item)
        } else {
            toDeleteList.add(item)
        }
    }
}