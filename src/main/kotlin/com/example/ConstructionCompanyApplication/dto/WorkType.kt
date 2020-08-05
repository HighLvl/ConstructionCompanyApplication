package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class WorkType(
    name: String? = null,
    id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("Название")
    val nameProperty = SimpleStringProperty()
    var name by nameProperty
    
}