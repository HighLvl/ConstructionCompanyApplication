package com.example.ConstructionCompanyApplication.dto

class MachineryModel(
    var name: String? = null,
    var machineryType: MachineryType? = null,
	id: Long? = null
) : AbstractDto(id)