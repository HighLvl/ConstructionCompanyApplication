package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class Brigade(
    name: String? = null,
    title: Title? = null,
    id: Long? = null
) : AbstractEntity(id) {
    val name = SimpleObjectProperty<String>(name)
    val title = SimpleObjectProperty<Title>(title)

    override fun toString(): String {
        return name.value.orEmpty()
    }


}