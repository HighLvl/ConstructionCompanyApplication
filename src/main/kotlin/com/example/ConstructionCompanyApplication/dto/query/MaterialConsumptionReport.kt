package com.example.ConstructionCompanyApplication.dto.query

import com.example.ConstructionCompanyApplication.dto.AbstractEntity
import com.example.ConstructionCompanyApplication.dto.Material
import javafx.beans.property.SimpleObjectProperty

class MaterialConsumptionReport(
    report: Report? = null,
    material: Material? = null,
    consAmount: Int? = null,
    estAmount: Int? = null,
    matConsOverrun: Int? = null
): AbstractEntity() {
    val report = SimpleObjectProperty<Report>(report)
    val material = SimpleObjectProperty<Material>(material)
    val consAmount = SimpleObjectProperty<Int>(consAmount)
    val estAmount = SimpleObjectProperty<Int>(estAmount)
    val matConsOverrun = SimpleObjectProperty<Int>(matConsOverrun)
}