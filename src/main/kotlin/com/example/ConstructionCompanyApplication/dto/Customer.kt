package com.example.ConstructionCompanyApplication.dto

class Customer(
    var name: String? = null,
    var phoneNumber: String? = null,
	id: Long? = null
) : AbstractDto(id)