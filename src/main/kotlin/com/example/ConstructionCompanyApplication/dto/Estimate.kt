package com.example.ConstructionCompanyApplication.dto

class Estimate(
    var amount: Int = 0,
    var workSchedule: WorkSchedule? = null,
    var material: Material? = null,
	id: Long? = null
) : AbstractDto(id)