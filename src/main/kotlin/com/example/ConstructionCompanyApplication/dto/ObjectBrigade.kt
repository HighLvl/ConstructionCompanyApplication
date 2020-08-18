package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty
import java.time.LocalDate

class ObjectBrigade(
    startDate: LocalDate? = null,
    finishDate: LocalDate? = null,
    brigade: Brigade? = null,
    buildObject: BuildObject? = null,
    workSchedule: WorkSchedule? = null,
    id: Long? = null
) : AbstractEntity(id) {

    val startDate = SimpleObjectProperty<LocalDate>(startDate)
    val finishDate = SimpleObjectProperty<LocalDate>(finishDate)
    val brigade = SimpleObjectProperty<Brigade>(brigade)
    val buildObject = SimpleObjectProperty<BuildObject>(buildObject)
    val workSchedule = SimpleObjectProperty<WorkSchedule>(workSchedule)

    override fun toString(): String {
        return toWordSequence(buildObject, brigade)
    }
}