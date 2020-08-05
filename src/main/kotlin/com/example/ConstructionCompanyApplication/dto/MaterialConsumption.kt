package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

class MaterialConsumption(
    amount: Int = 0,
    objectBrigade: ObjectBrigade = ObjectBrigade(),
    estimate: Estimate = Estimate(),
    id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("Количество")
    val amountProperty = SimpleIntegerProperty(amount)
    var amount by amountProperty

    @JsonIgnore
    val objectBrigadeProperty = SimpleObjectProperty<ObjectBrigade>(objectBrigade)
    var objectBrigade by objectBrigadeProperty

    @JsonIgnore
    val estimateProperty = SimpleObjectProperty<Estimate>(estimate)
    var estimate by estimateProperty

}