package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty

class Plot(
    chief: Staff? = null,
    management: Management? = null,
    id: Long? = null
) : AbstractEntity(id) {

    val chief = SimpleObjectProperty<Staff>(chief)
    val management = SimpleObjectProperty<Management>(management)

    override fun toString(): String {
        return toWordSequence(id, chief)
    }
}