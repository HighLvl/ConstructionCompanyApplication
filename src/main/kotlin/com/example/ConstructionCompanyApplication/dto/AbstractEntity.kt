package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty

abstract class AbstractEntity(id: Long? = null) {
    val id = SimpleObjectProperty<Long>(id)
}

fun toWordSequence(vararg words: ObjectProperty<*>?) =
    words.joinToString(", ", transform = { it?.value?.toString().orEmpty() })
