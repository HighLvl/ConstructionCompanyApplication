package com.example.ConstructionCompanyApplication.service

import com.example.ConstructionCompanyApplication.dto.*
import com.example.ConstructionCompanyApplication.dto.query.Report
import kotlin.reflect.KClass

object EntityEndpointMapper {
    private val endpointEntityMap = mutableMapOf<String, KClass<*>>()
    private val entityEndpointMap = mutableMapOf<KClass<*>, String>()

    init {
        putBidirectional(
            Brigade::class to "/brigades",
            BrigadeMember::class to "/brigadeMembers",
            BuildObject::class to "/buildObjects",
            Customer::class to "/customers",
            Estimate::class to "/estimates",
            Machinery::class to "/machines",
            MachineryModel::class to "/machineryModels",
            MachineryType::class to "/machineryTypes",
            Management::class to "/managements",
            Material::class to "/materials",
            MaterialConsumption::class to "/materialConsumptions",
            ObjectBrigade::class to "/objectBrigades",
            ObjectMachinery::class to "/objectMachines",
            Plot::class to "/plots",
            Prototype::class to "/prototypes",
            PrototypeType::class to "/prototypeTypes",
            Staff::class to "/staff",
            Title::class to "/titles",
            TitleCategory::class to "/titleCategories",
            WorkSchedule::class to "/workSchedules",
            WorkType::class to "/workTypes",
            Report::class to "/report"
        )
    }

    fun mapToEndpoint(entityClass: KClass<out AbstractEntity>) = entityEndpointMap[entityClass]!!

    @Suppress("UNCHECKED_CAST")
    fun mapToEntityClass(endpoint: String) = endpointEntityMap[endpoint]!! as KClass<out AbstractEntity>

    private fun putBidirectional(vararg pairs: Pair<KClass<*>, String>) {
        pairs.forEach { endpointEntityMap[it.second] = it.first; entityEndpointMap[it.first] = it.second }
    }
}