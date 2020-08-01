package com.example.ConstructionCompanyApplication.dto

import java.sql.Date

class ObjectBrigade(
    var startDate: Date? = null,
    var finishDate: Date? = null,
    var brigade: Brigade? = null,
    var buildObject: BuildObject? = null,
    var workSchedule: WorkSchedule? = null,
	id: Long? = null
) : AbstractDto(id)