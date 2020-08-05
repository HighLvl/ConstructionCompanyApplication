package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.time.LocalDate

class ObjectMachinery(
    startDate: LocalDate? = null,
    finishDate: LocalDate? = null,
    buildObject: BuildObject = BuildObject(),
    machinery: Machinery = Machinery(),
    id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("Начало работы")
    val startDateProperty = SimpleObjectProperty<LocalDate>(startDate)
    var startDate by startDateProperty

    @JsonIgnore
    @ColumnName("Конец работы")
    val finishDateProperty = SimpleObjectProperty<LocalDate>(finishDate)
    var finishDate by finishDateProperty

    @JsonIgnore
    val buildObjectProperty = SimpleObjectProperty<BuildObject>(buildObject)
    var buildObject by buildObjectProperty

    @JsonIgnore
    val machineryProperty = SimpleObjectProperty<Machinery>(machinery)
    var machinery by machineryProperty

    @JsonIgnore
    @ColumnName("Объект строительства")
    val BuildObjectIdProperty = buildObjectProperty.select(BuildObject::idProperty)

    @JsonIgnore
    @ColumnName("Строительная техника")
    val machineryModelProperty = machineryProperty.select(Machinery::modelNameProperty)

}