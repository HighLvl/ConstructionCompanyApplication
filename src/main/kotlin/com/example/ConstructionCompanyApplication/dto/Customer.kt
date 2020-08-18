package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty

class Customer(
    name: String? = null,
    phoneNumber: String? = null,
    id: Long? = null

) : AbstractEntity(id) {

    val name = SimpleObjectProperty<String>(name)
    val phoneNumber = SimpleObjectProperty<String>(phoneNumber)

    override fun toString(): String {
        return name.value.orEmpty()
    }
}