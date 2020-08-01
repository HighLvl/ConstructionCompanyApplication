package com.example.ConstructionCompanyApplication.dto

import java.sql.Date

class BuildObject(
    var startDate: Date? = null,
    var finishDate: Date? = null,
    var prototype: Prototype? = null,
    var plot: Plot? = null,
    var customer: Customer? = null,
	id: Long? = null
) : AbstractDto(id)