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

    @JsonIgnore
    @ColumnName("Бригада")
    val objectBrigadeNameProperty = objectBrigadeProperty.select(ObjectBrigade::brigadeNameProperty)

    @JsonIgnore
    @ColumnName("Материал")
    val estimateMaterialNameProperty = estimateProperty.select(Estimate::materialNameProperty)

    @JsonIgnore
    @ColumnName("Смета")
    val estimateAmountProperty = estimateProperty.select(Estimate::amountProperty)

}