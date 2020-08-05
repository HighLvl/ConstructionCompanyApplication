package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

class Prototype(
    deadline: Int = 0,
    prototypeType: PrototypeType = PrototypeType(),
    id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("Срок строительства")
    val deadlineProperty = SimpleIntegerProperty(deadline)
    var deadline by deadlineProperty

    @JsonIgnore
    val prototypeTypeProperty = SimpleObjectProperty<PrototypeType>(prototypeType)
    var prototypeType by prototypeTypeProperty

}