package com.example.ConstructionCompanyApplication.dto

class Plot(
    var chief: Staff? = null,
    var management: Management? = null,
	id: Long? = null
) : AbstractDto(id)