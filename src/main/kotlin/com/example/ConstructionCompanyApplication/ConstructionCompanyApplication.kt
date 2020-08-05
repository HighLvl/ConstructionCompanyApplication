package com.example.ConstructionCompanyApplication


import com.example.ConstructionCompanyApplication.ui.view.BuildObjectView
import com.example.ConstructionCompanyApplication.ui.view.MaterialConsumptionView
import tornadofx.*

class MyApp : App(MaterialConsumptionView::class)

fun main(args: Array<String>) {
    launch<MyApp>(args)
}