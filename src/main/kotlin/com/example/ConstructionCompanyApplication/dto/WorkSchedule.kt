package com.example.ConstructionCompanyApplication.dto

class WorkSchedule(
    var ord: Int = 0,
    var deadline: Int = 0,
    var prototype: Prototype? = null,
    var workType: WorkType? = null,
	id: Long? = null
) : AbstractDto(id)