package com.example.ConstructionCompanyApplication.service

class RsqlFilterBuilder {
    private val expressionList = mutableListOf<Triple<String, String, Any>>()


    private fun isEmpty(value: Any?) = value == null || value.toString().isEmpty()

    fun equal(prop: String, value: Any?): RsqlFilterBuilder {
        if (isEmpty(value)) return this
        expressionList += Triple(prop, EQUAL, value!!)
        return this
    }

    fun substring(prop: String, value: Any?): RsqlFilterBuilder {
        if (isEmpty(value)) return this

        expressionList += Triple(prop, EQUAL, "*${value!!}*")
        return this
    }


    fun greaterOrEqual(prop: String, value: Any?): RsqlFilterBuilder {
        if (isEmpty(value)) return this
        expressionList += Triple(prop, GREATER_OR_EQUAL, value!!)
        return this
    }

    fun lessOrEqual(prop: String, value: Any?): RsqlFilterBuilder {
        if (isEmpty(value)) return this
        expressionList += Triple(prop, LESS_OR_EQUAL, value!!)
        "".isNullOrEmpty()
        return this
    }

    fun build(): String {
        return expressionList.joinToString(AND) { it.first + it.second + it.third.toString() }
    }

    private companion object {
        const val EQUAL = "=="
        const val GREATER_OR_EQUAL = ">="
        const val LESS_OR_EQUAL = "<="
        const val AND = ";"
    }
}