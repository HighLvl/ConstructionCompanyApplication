package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import java.time.LocalDate
import tornadofx.*

class BrigadeMember(
    startDate: LocalDate? = null,
    finishDate: LocalDate? = null,
    isBrigadier: Boolean = false,
    brigade: Brigade = Brigade(),
    staff: Staff = Staff(),
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
    @ColumnName("Бригадир")
    val isBrigadierProperty = SimpleBooleanProperty(isBrigadier)
    var isBrigadier by isBrigadierProperty

    @JsonIgnore
    val brigadeProperty = SimpleObjectProperty<Brigade>(brigade)
    var brigade by brigadeProperty

    @JsonIgnore
    val staffProperty = SimpleObjectProperty<Staff>(staff)
    var staff by staffProperty

    @JsonIgnore
    @ColumnName("Бригада")
    val brigadeNameProperty = brigadeProperty.select(Brigade::nameProperty)

    @JsonIgnore
    @ColumnName("ФИО")
    val staffNameProperty = staffProperty.select(Staff::nameProperty)
}