package com.example.ConstructionCompanyApplication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import javafx.beans.property.SimpleObjectProperty
import java.time.LocalDate
import tornadofx.*

class BrigadeMember(
    startDate: LocalDate? = null,
    finishDate: LocalDate? = null,
    isBrigadier: Boolean? = null,
    brigade: Brigade? = null,
    staff: Staff? = null,
    id: Long? = null
) : AbstractEntity(id) {
    val startDate = SimpleObjectProperty<LocalDate>(startDate)
    val finishDate = SimpleObjectProperty<LocalDate>(finishDate)
    @get:JsonProperty("isBrigadier")
    var isBrigadier = SimpleObjectProperty<Boolean>(isBrigadier)
    val brigade = SimpleObjectProperty<Brigade>(brigade)
    val staff = SimpleObjectProperty<Staff>(staff)

    override fun toString(): String {
        return staff.toString()
    }
}