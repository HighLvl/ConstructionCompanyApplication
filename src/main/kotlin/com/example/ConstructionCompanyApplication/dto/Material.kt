package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

class Material(
    name: String? = null,
	id: Long? = null
) : AbstractEntity(id) {
    val name = SimpleObjectProperty<String>(name)

    override fun toString(): String {
        return name.value.orEmpty()
    }
}