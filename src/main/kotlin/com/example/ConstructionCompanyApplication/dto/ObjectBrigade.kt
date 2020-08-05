package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.time.LocalDate

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

    @JsonIgnore
    @ColumnName("Бригада")
    val brigadeNameProperty = brigadeProperty.select(Brigade::nameProperty)

    @JsonIgnore
    @ColumnName("Номер объекта строительства")
    val buildObjectIdProperty = buildObjectProperty.select(BuildObject::idProperty)

    @JsonIgnore
    @ColumnName("График работ")
    val workScheduleWorkTypeNameProperty = workScheduleProperty.select(WorkSchedule::workTypeNameProperty)

}