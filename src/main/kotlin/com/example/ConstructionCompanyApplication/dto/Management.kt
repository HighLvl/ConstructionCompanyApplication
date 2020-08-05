package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

class Management(
    chief: Staff = Staff(),
	id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    val chiefProperty = SimpleObjectProperty<Staff>(chief)
    var chief by chiefProperty

    @JsonIgnore
    @ColumnName("Номер")
    val idProperty = SimpleLongProperty(id?:0)
    override var id: Long? by idProperty

    @JsonIgnore
    @ColumnName("Начальник")
    val chiefNameProperty = chiefProperty.select(Staff::nameProperty)

}