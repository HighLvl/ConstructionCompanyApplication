package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty

class Estimate(
    amount: Int? = null,
    workSchedule: WorkSchedule? = null,
    material: Material? = null,
    id: Long? = null
) : AbstractEntity(id) {

    val amount = SimpleObjectProperty<Int>(amount)
    val workSchedule = SimpleObjectProperty<WorkSchedule>(workSchedule)
    val material = SimpleObjectProperty<Material>(material)

    override fun toString(): String {
        return toWordSequence(amount, material)
    }
}