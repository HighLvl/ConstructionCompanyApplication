package com.example.ConstructionCompanyApplication.ui.configuration

import com.example.ConstructionCompanyApplication.ui.view.*
import com.example.ConstructionCompanyApplication.ui.view.filter.FilterViewFactory

class EntityConfiguration(
    val tableViewFactory: TableViewFactory,
    val entityMetadata: EntityMetadata,
    val filterViewFactory: FilterViewFactory? = null
)