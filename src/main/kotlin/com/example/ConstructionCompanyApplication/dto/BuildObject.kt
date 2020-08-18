package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty
import java.time.LocalDate

class BuildObject(
    startDate: LocalDate? = null,
    finishDate: LocalDate? = null,
    prototype: Prototype? = null,
    plot: Plot? = null,
    customer: Customer? = null,
    id: Long? = null
) : AbstractEntity(id) {
    val startDate = SimpleObjectProperty<LocalDate>(startDate)
    val finishDate = SimpleObjectProperty<LocalDate>(finishDate)
    val prototype = SimpleObjectProperty<Prototype>(prototype)
    val plot = SimpleObjectProperty<Plot>(plot)
    val customer = SimpleObjectProperty<Customer>(customer)

    override fun toString(): String {
        return id.value?.toString().orEmpty()
    }
}