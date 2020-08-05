package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.time.LocalDate

class MachineryModel(
    name: String? = null,
    machineryType: MachineryType = MachineryType(),
    id: Long? = null
) : AbstractDto(id){
    @JsonIgnore
    @ColumnName("Модель")
    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    @JsonIgnore
    val machineryTypeProperty = SimpleObjectProperty<MachineryType>(machineryType)
    var machineryType by machineryTypeProperty

    @JsonIgnore
    @ColumnName("Тип")
    val machineryTypeNameProperty =  machineryTypeProperty.select(MachineryType::nameProperty)

    @JsonIgnore
    @ColumnName("Дата")
    val dateProperty =  SimpleObjectProperty<LocalDate>(LocalDate.MAX)

}