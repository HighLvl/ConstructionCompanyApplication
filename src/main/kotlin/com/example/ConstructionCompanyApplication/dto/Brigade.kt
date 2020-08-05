package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Brigade(
    name: String? = null,
    title: Title = Title(),
    id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("Название")
    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    @JsonIgnore
    val tittleProperty = SimpleObjectProperty<Title>(title)
    var title by tittleProperty
}