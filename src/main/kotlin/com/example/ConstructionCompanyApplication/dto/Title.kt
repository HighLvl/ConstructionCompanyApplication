package com.example.ConstructionCompanyApplication.dto

class Title(
    var name: String? = null,
    var titleCategory: TitleCategory? = null,
	id: Long? = null
) : AbstractDto(id)