package com.example.ConstructionCompanyApplication.dto

import java.sql.Date

class BrigadeMember(
    var startDate: Date? = null,
    var finishDate: Date? = null,
    var isBrigadier: Boolean = false,
    var brigade: Brigade? = null,
    var staff: Staff? = null,
	id: Long? = null
) : AbstractDto(id)