package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleObjectProperty
import java.time.LocalDate
import tornadofx.getValue
import tornadofx.setValue

class BuildObject(
    startDate: LocalDate? = null,
    finishDate: LocalDate? = null,
    prototype: Prototype = Prototype(),
    plot: Plot = Plot(),
    customer: Customer = Customer(),
    id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("Начало строительства")
    val startDateProperty = SimpleObjectProperty<LocalDate>(startDate)
    var startDate by startDateProperty
    @JsonIgnore
    @ColumnName("Конец строительства")
    val finishDateProperty = SimpleObjectProperty<LocalDate>(finishDate)
    var finishDate by finishDateProperty
    @JsonIgnore
    val prototypeProperty = SimpleObjectProperty<Prototype>(prototype)
    var prototype by prototypeProperty
    @JsonIgnore
    val plotProperty = SimpleObjectProperty<Plot>(plot)
    var plot by plotProperty
    @JsonIgnore
    val customerProperty = SimpleObjectProperty<Customer>(customer)
    var customer by customerProperty

}