package com.example.ConstructionCompanyApplication.ui.view

import com.example.ConstructionCompanyApplication.controller.CommonController
import io.reactivex.Single
import javafx.scene.control.Label
import javafx.scene.control.MenuButton
import javafx.scene.control.MenuItem
import javafx.scene.control.Pagination
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
    lateinit var loadPage: (pageIndex: Int, pageSize: Int) -> Single<CommonController.PageInfo>

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
        loadPage.invoke(pageIndex, pageSize)
            .subscribe(
                { pageInfo ->
                    val totalPages = pageInfo.totalPages.toInt()
                    pagination.pageCount = if (totalPages == 0) 1 else totalPages
                    totalElementsLabel.text = pageInfo.totalElements.toString()
                },
                {

                }
            )
    }
}