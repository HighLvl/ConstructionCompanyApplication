package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty

class MachineryModel(
    name: String? = null,
    machineryType: MachineryType? = null,
    id: Long? = null
) : AbstractEntity(id) {

    val name = SimpleObjectProperty<String>(name)
    val machineryType = SimpleObjectProperty<MachineryType>(machineryType)

    override fun toString(): String {
        return name.value.orEmpty()
    }
}