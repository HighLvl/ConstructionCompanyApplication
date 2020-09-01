package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty

class Management(
    chief: Staff? = null,
    id: Long? = null
) : AbstractEntity(id) {

    val chief = SimpleObjectProperty<Staff>(chief)

    override fun toString(): String {
        return toWordSequence(id, chief)
    }
}