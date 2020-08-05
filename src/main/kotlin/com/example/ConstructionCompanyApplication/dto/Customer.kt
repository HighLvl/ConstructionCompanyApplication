package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Customer(
    name: String? = null,
    phoneNumber: String? = null,
    id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("ФИО")
    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    @JsonIgnore
    @ColumnName("Номер телефона")
    val phoneNumberProperty = SimpleStringProperty(phoneNumber)
    var phoneNumber by phoneNumberProperty

}