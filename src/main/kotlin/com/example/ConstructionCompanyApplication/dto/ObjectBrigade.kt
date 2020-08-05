package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleObjectProperty
import java.sql.Date
import java.time.LocalDate
import tornadofx.getValue
import tornadofx.setValue

class ObjectBrigade(
    startDate: LocalDate? = null,
    finishDate: LocalDate? = null,
    brigade: Brigade = Brigade(),
    buildObject: BuildObject = BuildObject(),
    workSchedule: WorkSchedule = WorkSchedule(),
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
    val brigadeProperty = SimpleObjectProperty<Brigade>(brigade)
    var brigade by brigadeProperty
    @JsonIgnore
    val buildObjectProperty = SimpleObjectProperty<BuildObject>(buildObject)
    var buildObject by buildObjectProperty
    @JsonIgnore
    val workScheduleProperty = SimpleObjectProperty<WorkSchedule>(workSchedule)
    var workSchedule by workScheduleProperty
}