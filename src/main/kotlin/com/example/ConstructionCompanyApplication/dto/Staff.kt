package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class Staff(
    name: String? = null,
    phoneNumber: String? = null,
    title: Title? = Title(),
    chief: Staff? = Staff(),
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

    @JsonIgnore
    val titleProperty = SimpleObjectProperty<Title>(title)
    var title by titleProperty

    @JsonIgnore
    val chiefProperty = SimpleObjectProperty<Staff>(chief)
    var chief by chiefProperty

}