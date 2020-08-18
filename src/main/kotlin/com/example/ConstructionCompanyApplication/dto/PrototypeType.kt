package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty

class PrototypeType(
    name: String? = null,
    id: Long? = null
) : AbstractEntity(id) {
    val name = SimpleObjectProperty<String>(name)

    override fun toString(): String {
        return name.value
    }
}