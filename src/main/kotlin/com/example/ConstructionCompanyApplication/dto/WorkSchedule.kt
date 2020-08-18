package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty

class WorkSchedule(
    ord: Int? = null,
    deadline: Int? = null,
    prototype: Prototype? = null,
    workType: WorkType? = null,
    id: Long? = null
) : AbstractEntity(id) {
    val ord = SimpleObjectProperty<Int>(ord)
    val deadline = SimpleObjectProperty<Int>(deadline)
    val prototype = SimpleObjectProperty<Prototype>(prototype)
    val workType = SimpleObjectProperty<WorkType>(workType)

    override fun toString(): String {
        return toWordSequence(ord, workType)
    }
}