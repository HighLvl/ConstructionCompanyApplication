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
    material: Material? = null,
    consAmount: Int? = null,
    estAmount: Int? = null,
    matConsOverrun: Int? = null
): AbstractEntity() {
    val brigade = SimpleObjectProperty<Brigade>(brigade)
    val buildObject = SimpleObjectProperty<BuildObject>(buildObject)
    val workType = SimpleObjectProperty<WorkType>(workType)
    val startDate = SimpleObjectProperty<LocalDate>(startDate)
    val finishDate = SimpleObjectProperty<LocalDate>(finishDate)
    val deadline = SimpleObjectProperty<Int>(deadline)
    val timeOverrun = SimpleObjectProperty<Int>(timeOverrun)
    val material = SimpleObjectProperty<Material>(material)
    val consAmount = SimpleObjectProperty<Int>(consAmount)
    val estAmount = SimpleObjectProperty<Int>(estAmount)
    val matConsOverrun = SimpleObjectProperty<Int>(matConsOverrun)
}