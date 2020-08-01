package com.example.ConstructionCompanyApplication.dto

class Prototype(
    var deadline: Int = 0,
    var prototypeType: PrototypeType? = null,
	id: Long? = null
) : AbstractDto(id)