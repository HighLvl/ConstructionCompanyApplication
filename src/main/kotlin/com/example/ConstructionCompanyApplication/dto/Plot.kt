package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class Plot(
    chief: Staff = Staff(),
    management: Management = Management(),
    id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("Номер")
    val idProperty = SimpleLongProperty(id ?: 0)
    override var id: Long? by idProperty

    @JsonIgnore
    val chiefProperty = SimpleObjectProperty<Staff>(chief)
    var chief by chiefProperty

    @JsonIgnore
    val managementProperty = SimpleObjectProperty<Management>(management)
    var management by managementProperty

    @JsonIgnore
    @ColumnName("Начальник")
    val chiefNameProperty = chiefProperty.select(Staff::nameProperty)

    @JsonIgnore
    @ColumnName("Строительное управление")
    val managementIdProperty = managementProperty.select(Management::idProperty)

}