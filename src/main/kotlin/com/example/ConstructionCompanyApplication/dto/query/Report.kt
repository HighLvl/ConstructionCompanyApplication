package com.example.ConstructionCompanyApplication.dto.query

import com.example.ConstructionCompanyApplication.dto.*
import javafx.beans.property.SimpleObjectProperty
import java.time.LocalDate

class Report(
    brigade: Brigade? = null,
    buildObject : BuildObject? = null,
    workType: WorkType? = null,
    startDate: LocalDate? = null,
    finishDate: LocalDate? = null,
    deadline: Int? = null,
    timeOverrun: Int? = null,
    id: Long? = null
): AbstractEntity(id) {
    val brigade = SimpleObjectProperty<Brigade>(brigade)
    val buildObject = SimpleObjectProperty<BuildObject>(buildObject)
    val workType = SimpleObjectProperty<WorkType>(workType)
    val startDate = SimpleObjectProperty<LocalDate>(startDate)
    val finishDate = SimpleObjectProperty<LocalDate>(finishDate)
    val deadline = SimpleObjectProperty<Int>(deadline)
    val timeOverrun = SimpleObjectProperty<Int>(timeOverrun)

    override fun toString(): String {
        return id.value.toString()
    }
}