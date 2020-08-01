package com.example.ConstructionCompanyApplication.dto

class MaterialConsumption(
    var amount: Int = 0,
    var objectBrigade: ObjectBrigade? = null,
    var estimate: Estimate? = null,
	id: Long? = null
) : AbstractDto(id)