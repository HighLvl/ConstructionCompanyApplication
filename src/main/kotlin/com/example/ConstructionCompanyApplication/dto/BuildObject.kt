package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.time.LocalDate

class BuildObject(
    startDate: LocalDate = LocalDate.now(),
    finishDate: LocalDate = LocalDate.now(),
    prototype: Prototype = Prototype(),
    plot: Plot = Plot(),
    customer: Customer = Customer(),
    id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("Номер")
    val idProperty = SimpleLongProperty(id ?: 0)
    override var id: Long? by idProperty

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

    @JsonIgnore
    @ColumnName("Номер прототипа")
    val prototypeIdProperty = prototypeProperty.select(Prototype::idProperty)


    @JsonIgnore
    @ColumnName("Номер участка")
    val plotIdProperty = plotProperty.select(Plot::idProperty)

    @JsonIgnore
    @ColumnName("Заказчик")
    val customerNameProperty = customerProperty.select(Customer::nameProperty)


}