package com.example.ConstructionCompanyApplication.ui.view

import com.example.ConstructionCompanyApplication.controller.CommonController
import javafx.scene.control.*
import tornadofx.*

class EntityTableViewPagination(
    private val pagination: Pagination,
    private val tableView: EntityTableView<*>,
    private val pageSizeMenu: MenuButton,
    pageSize25: MenuItem,
    pageSize50: MenuItem,
    pageSize100: MenuItem,
    private val totalElementsLabel: Label

    ) {
    private var pageSize = 25
    lateinit var pageLoader: (pageIndex: Int, pageSize: Int) -> CommonController.PageInfo

    init {
        pagination.setPageFactory { pageIndex ->
            loadPage(pageIndex)
            tableView
        }

        fun changePageSize(pageSize: Int) {
            this.pageSize = pageSize
            pageSizeMenu.text = pageSize.toString()
            pagination.pageFactory.call(0)
        }

        pageSizeMenu.text = pageSize.toString()
        pageSize25.action { changePageSize(25) }
        pageSize50.action { changePageSize(50) }
        pageSize100.action { changePageSize(100) }
    }

    fun loadPage(pageIndex: Int) {
        val pageInfo = pageLoader.invoke(pageIndex, pageSize)
        val totalPages = pageInfo.totalPages.toInt()
        pagination.pageCount = if (totalPages == 0) 1 else totalPages
        totalElementsLabel.text = pageInfo.totalElements.toString()
    }
}