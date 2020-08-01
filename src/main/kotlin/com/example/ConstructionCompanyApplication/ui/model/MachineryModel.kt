package com.example.ConstructionCompanyApplication.ui.model

import com.example.ConstructionCompanyApplication.dto.MachineryModel
import javafx.beans.property.SimpleStringProperty
import tornadofx.ItemViewModel

class MachineryModelModel: ItemViewModel<MachineryModel>() {
    val name = bind {SimpleStringProperty(item?.name ?: "")}
    val type = bind {SimpleStringProperty(item?.machineryType?.name ?: "")}
}