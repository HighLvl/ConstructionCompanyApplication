package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty

class MaterialConsumption(
    amount: Int? = null,
    objectBrigade: ObjectBrigade? = null,
    estimate: Estimate? = null,
    id: Long? = null
) : AbstractEntity(id) {

    val amount = SimpleObjectProperty<Int>(amount)
    val objectBrigade = SimpleObjectProperty<ObjectBrigade>(objectBrigade)
    val estimate = SimpleObjectProperty<Estimate>(estimate)

    override fun toString(): String {
        return toWordSequence(amount, estimate)
    }
}