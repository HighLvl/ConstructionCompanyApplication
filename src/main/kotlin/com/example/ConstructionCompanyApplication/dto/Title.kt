package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty

class Title(
    name: String? = null,
    titleCategory: TitleCategory? = null,
    id: Long? = null
) : AbstractEntity(id) {

    val name = SimpleObjectProperty<String>(name)
    val titleCategory = SimpleObjectProperty<TitleCategory>(titleCategory)

    override fun toString(): String {
        return name.value
    }
}