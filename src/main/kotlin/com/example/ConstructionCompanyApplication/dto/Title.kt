package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class Title(
    name: String? = null,
    titleCategory: TitleCategory = TitleCategory(),
	id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("Название")
    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    @JsonIgnore
    val titleCategoryProperty = SimpleObjectProperty<TitleCategory>(titleCategory)
    var titleCategory by titleCategoryProperty

}