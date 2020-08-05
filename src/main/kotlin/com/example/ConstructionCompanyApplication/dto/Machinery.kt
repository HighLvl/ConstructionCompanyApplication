package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

class Machinery(
    model: MachineryModel = MachineryModel(),
    management: Management = Management(),
    id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    val modelProperty = SimpleObjectProperty<MachineryModel>(model)
    var model by modelProperty
    @JsonIgnore
    val managementProperty = SimpleObjectProperty<Management>(management)
    var management by managementProperty

}