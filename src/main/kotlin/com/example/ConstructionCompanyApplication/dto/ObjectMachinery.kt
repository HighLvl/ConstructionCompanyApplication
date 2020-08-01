package com.example.ConstructionCompanyApplication.dto

import java.sql.Date

class ObjectMachinery(
    var startDate: Date? = null,
    var finishDate: Date? = null,
    var buildObject: BuildObject? = null,
    var machinery: Machinery? = null,
	id: Long? = null
) : AbstractDto(id)