package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty

class Machinery(
    model: MachineryModel? = null,
    management: Management? = null,
    id: Long? = null
) : AbstractEntity(id) {
    val model = SimpleObjectProperty<MachineryModel>(model)
    val management = SimpleObjectProperty<Management>(management)

    override fun toString(): String {
        return toWordSequence(id, model)
    }
}