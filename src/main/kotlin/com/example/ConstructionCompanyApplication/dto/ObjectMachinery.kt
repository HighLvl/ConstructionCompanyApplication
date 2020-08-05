package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleObjectProperty
import java.sql.Date
import java.time.LocalDate
import tornadofx.getValue
import tornadofx.setValue

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

}