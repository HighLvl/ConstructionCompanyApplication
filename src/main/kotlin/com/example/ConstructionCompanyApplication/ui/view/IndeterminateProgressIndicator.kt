package com.example.ConstructionCompanyApplication.ui.view

import io.reactivex.Single
import javafx.geometry.Pos
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import tornadofx.*

fun <T> Single<T>.setProgressIndicator(root: StackPane): Single<T> {
    val progressIndicator = ProgressIndicator()
    val box = VBox(progressIndicator)
    box.alignment = Pos.CENTER

    return this.doOnSubscribe {
        root.children.forEach { it.isDisable = true }
        root.children.add(box)
    }.doOnTerminate {
        box.removeFromParent()
        root.children.forEach { it.isDisable = false }

    }
}