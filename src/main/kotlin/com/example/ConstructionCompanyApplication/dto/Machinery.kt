package com.example.ConstructionCompanyApplication.dto

class Machinery(
    var model: MachineryModel? = null,
    var management: Management? = null,
	id: Long? = null
) : AbstractDto(id)