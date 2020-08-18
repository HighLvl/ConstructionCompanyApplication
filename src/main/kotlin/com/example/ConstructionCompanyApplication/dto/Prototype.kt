package com.example.ConstructionCompanyApplication.dto

import javafx.beans.property.SimpleObjectProperty

class Prototype(
    deadline: Int? = null,
    prototypeType: PrototypeType? = null,
    id: Long? = null
) : AbstractEntity(id) {

    val deadline = SimpleObjectProperty<Int>(deadline)
    val prototypeType = SimpleObjectProperty<PrototypeType>(prototypeType)

    override fun toString(): String {
        return toWordSequence(id, prototypeType)
    }
}