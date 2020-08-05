package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class Estimate(
    amount: Int = 0,
    workSchedule: WorkSchedule = WorkSchedule(),
    material: Material = Material(),
    id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("Количество")
    val amountProperty = SimpleIntegerProperty(amount)
    var amount by amountProperty

    @JsonIgnore
    val workScheduleProperty = SimpleObjectProperty<WorkSchedule>(workSchedule)
    var workSchedule by workScheduleProperty

    @JsonIgnore
    val materialProperty = SimpleObjectProperty<Material>(material)
    var material by materialProperty

    @JsonIgnore
    @ColumnName("График работ")
    val workScheduleIdProperty = workScheduleProperty.select(WorkSchedule::workTypeNameProperty)

    @JsonIgnore
    @ColumnName("Материал")
    val materialNameProperty = materialProperty.select(Material::nameProperty)

}