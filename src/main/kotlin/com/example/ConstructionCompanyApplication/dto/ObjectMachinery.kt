package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty
import java.time.LocalDate

class ObjectMachinery(
    startDate: LocalDate? = null,
    finishDate: LocalDate? = null,
    buildObject: BuildObject? = null,
    machinery: Machinery? = null,
    id: Long? = null
) : AbstractEntity(id) {

    val startDate = SimpleObjectProperty<LocalDate>(startDate)
    val finishDate = SimpleObjectProperty<LocalDate>(finishDate)
    val buildObject = SimpleObjectProperty<BuildObject>(buildObject)
    val machinery = SimpleObjectProperty<Machinery>(machinery)

    override fun toString(): String {
        return toWordSequence(buildObject, machinery)
    }
}