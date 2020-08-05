package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

class WorkSchedule(
    ord: Int = 0,
    deadline: Int = 0,
    prototype: Prototype = Prototype(),
    workType: WorkType = WorkType(),
    id: Long? = null
) : AbstractDto(id) {
    @JsonIgnore
    @ColumnName("Порядок выполнения")
    val ordProperty = SimpleIntegerProperty(ord)
    var ord by ordProperty

    @JsonIgnore
    @ColumnName("Срок выполнения")
    val deadlineProperty = SimpleIntegerProperty(deadline)
    var deadline by deadlineProperty

    @JsonIgnore
    val prototypeProperty = SimpleObjectProperty<Prototype>(prototype)
    var prototype by prototypeProperty

    @JsonIgnore
    val workTypeProperty = SimpleObjectProperty<WorkType>(workType)
    var workType by workTypeProperty

}