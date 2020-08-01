package com.example.ConstructionCompanyApplication.dto

import java.io.Serializable

abstract class AbstractDto(var id: Long? = null) {


    override fun toString() = "Entity of type ${this.javaClass.name} with id: $id"
    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (this === other) return true
        other as AbstractDto
        return id == other.id
    }

    override fun hashCode(): Int {
        return 31
    }

    companion object {
        private val serialVersionUID = -5554308939380869754L
    }
}