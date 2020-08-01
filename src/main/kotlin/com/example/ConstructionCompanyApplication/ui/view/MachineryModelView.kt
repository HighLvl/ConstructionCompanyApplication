package com.example.ConstructionCompanyApplication.ui.view

import com.example.ConstructionCompanyApplication.dto.MachineryModel
import tornadofx.*

class MachineryModelView : View("Модели строительной техники") {
    override val root = borderpane {
        tableview<MachineryModel> {
            
        }
    }
}
