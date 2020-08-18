package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty

class Staff(
    name: String? = null,
    phoneNumber: String? = null,
    title: Title? = null,
    chief: Staff? = null,
    id: Long? = null
) : AbstractEntity(id) {

    val name = SimpleObjectProperty<String>(name)
    val phoneNumber = SimpleObjectProperty<String>(phoneNumber)
    val title = SimpleObjectProperty<Title>(title)
    val chief = SimpleObjectProperty<Staff>(chief)

    override fun toString(): String {
        return name.value.orEmpty()
    }
}