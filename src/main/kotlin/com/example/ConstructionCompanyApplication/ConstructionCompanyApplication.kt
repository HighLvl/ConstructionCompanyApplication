package com.example.ConstructionCompanyApplication


import com.example.ConstructionCompanyApplication.ui.view.MachineryModelView
import tornadofx.*

class MyApp : App(MachineryModelView::class)
fun main(args: Array<String>) {
    launch<MyApp>(args)
}