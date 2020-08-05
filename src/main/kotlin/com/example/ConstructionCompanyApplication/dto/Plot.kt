package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

class Plot(
    chief: Staff = Staff(),
    management: Management = Management(),
    id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("Номер")
    val idProperty = SimpleLongProperty(id?:0)

    @JsonIgnore
    val chiefProperty = SimpleObjectProperty<Staff>(chief)
    var chief by chiefProperty

    @JsonIgnore
    val managementProperty = SimpleObjectProperty<Management>(management)
    var management by managementProperty

}