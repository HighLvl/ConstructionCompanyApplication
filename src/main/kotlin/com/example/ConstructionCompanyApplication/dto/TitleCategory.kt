package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class TitleCategory(
    name: String? = null,
	id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("Название")
    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty
}