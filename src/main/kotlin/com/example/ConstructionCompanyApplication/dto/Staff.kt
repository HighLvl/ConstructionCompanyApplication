package com.example.ConstructionCompanyApplication.dto

class Staff(
    var name: String? = null,
    var phoneNumber: String? = null,
    var title: Title? = null,
    var chief: Staff? = null,
	id: Long? = null
) : AbstractDto(id)