package com.example.ConstructionCompanyApplication.ui.configuration

import com.example.ConstructionCompanyApplication.dto.*
import com.example.ConstructionCompanyApplication.dto.query.MaterialConsumptionReport
import com.example.ConstructionCompanyApplication.dto.query.Report
import com.example.ConstructionCompanyApplication.ui.view.*
import com.example.ConstructionCompanyApplication.ui.view.filter.BuildObjectFilterViewFactory
import com.example.ConstructionCompanyApplication.ui.view.filter.MaterialConsumptionReportFilterViewFactory
import com.example.ConstructionCompanyApplication.ui.view.filter.ReportFilterViewFactory
import kotlin.reflect.KClass

/*Предоставляет по классу сущности конфигурацию, содержащую фабрики для создания специфичных view, а так же другие метаданные*/
object EntityConfigurationProvider {
    private val configurationMap: Map<KClass<*>, EntityConfiguration> = mapOf(
        Brigade::class to EntityConfiguration(
            BrigadeTableViewFactory(),
            EntityMetadata("Бригада", listOf(Brigade::name))
        ),
        BrigadeMember::class to EntityConfiguration(
            BrigadeMemberTableViewFactory(),
            EntityMetadata(
                "Участник бригады", listOf(
                    BrigadeMember::staff,
                    BrigadeMember::brigade,
                    BrigadeMember::startDate,
                    BrigadeMember::finishDate
                )
            )
        ),
        BuildObject::class to EntityConfiguration(
            BuildObjectTableViewFactory(),
            EntityMetadata(
                "Строительный объект", listOf(BuildObject::prototype, BuildObject::id)
            ),
            BuildObjectFilterViewFactory()
        ),
        Customer::class to EntityConfiguration(
            CustomerTableViewFactory(),
            EntityMetadata("Заказчик", listOf(Customer::name))
        ),
        Estimate::class to EntityConfiguration(
            EstimateTableViewFactory(),
            EntityMetadata("Смета", listOf(Estimate::material, Estimate::amount))
        ),
        Machinery::class to EntityConfiguration(
            MachineryTableViewFactory(),
            EntityMetadata("Строительная техника", listOf(Machinery::model, Machinery::id))
        ),
        MachineryModel::class to EntityConfiguration(
            MachineryModelTableViewFactory(),
            EntityMetadata("Модель строительной техники", listOf(MachineryModel::name))
        ),
        MachineryType::class to EntityConfiguration(
            MachineryTypeTableViewFactory(),
            EntityMetadata("Вид строительной техники", listOf(MachineryType::name))
        ),
        Management::class to EntityConfiguration(
            ManagementTableViewFactory(),
            EntityMetadata("Строительное управление")
        ),
        Material::class to EntityConfiguration(
            MaterialTableViewFactory(),
            EntityMetadata("Материал", listOf(Material::name))
        ),
        MaterialConsumption::class to EntityConfiguration(
            MaterialConsumptionTableViewFactory(),
            EntityMetadata("Расход материалов", listOf(MaterialConsumption::estimate, MaterialConsumption::amount))
        ),
        ObjectBrigade::class to EntityConfiguration(
            ObjectBrigadeTableViewFactory(),
            EntityMetadata("История работ бригады", listOf(ObjectBrigade::buildObject, ObjectBrigade::brigade))
        ),
        ObjectMachinery::class to EntityConfiguration(
            ObjectMachineryTableViewFactory(),
            EntityMetadata(
                "История работ строительной техники",
                listOf(ObjectMachinery::buildObject, ObjectMachinery::machinery)
            )
        ),
        Plot::class to EntityConfiguration(
            PlotTableViewFactory(),
            EntityMetadata("Участок")
        ),
        Prototype::class to EntityConfiguration(
            PrototypeTableViewFactory(),
            EntityMetadata("Проект", listOf(Prototype::prototypeType))
        ),
        PrototypeType::class to EntityConfiguration(
            PrototypeTypeTableViewFactory(),
            EntityMetadata("Вид проекта", listOf(PrototypeType::name))
        ),
        Staff::class to EntityConfiguration(
            StaffTableViewFactory(),
            EntityMetadata("Сотрудник", listOf(Staff::name))
        ),
        Title::class to EntityConfiguration(
            TitleTableViewFactory(),
            EntityMetadata("Должность", listOf(Title::name, Title::titleCategory))
        ),
        TitleCategory::class to EntityConfiguration(
            TitleCategoryTableViewFactory(),
            EntityMetadata("Категория должности", listOf(TitleCategory::name))
        ),
        WorkSchedule::class to EntityConfiguration(
            WorkScheduleTableViewFactory(),
            EntityMetadata("Рабочий график", listOf(WorkSchedule::ord, WorkSchedule::workType))
        ),
        WorkType::class to EntityConfiguration(
            WorkTypeTableViewFactory(),
            EntityMetadata("Вид работ", listOf(WorkType::name))
        ),
        Report::class to EntityConfiguration(
            ReportTableViewFactory(),
            EntityMetadata("Отчет"),
            ReportFilterViewFactory()
        ),
        MaterialConsumptionReport::class to EntityConfiguration(
            MaterialConsumptionReportTableViewFactory(),
            EntityMetadata("Отчет по расходу материалов"),
            MaterialConsumptionReportFilterViewFactory()
        )
    )

    val mapOfEntitiesByGroup = mapOf(
        "Сотрудники" to listOf(Staff::class, Title::class, TitleCategory::class),
        "Заказчики" to listOf(Customer::class),
        "Бригады" to listOf(Brigade::class, BrigadeMember::class, ObjectBrigade::class),
        "Строительные работы" to listOf(
            WorkType::class,
            WorkSchedule::class,
            Estimate::class,
            MaterialConsumption::class
        ),
        "Объекты строительства" to listOf(BuildObject::class, Prototype::class, PrototypeType::class),
        "Строительные управления" to listOf(Management::class, Plot::class),
        "Строительная техника" to listOf(
            MachineryModel::class,
            MachineryType::class,
            Machinery::class,
            ObjectMachinery::class
        ),
        "Строительные материалы" to listOf(Material::class)
    )

    val mapOfQueries = mapOf(
        "Отчет" to Report::class,
        "Отчет по расходу материалов" to MaterialConsumptionReport::class
    )

    fun get(entityClass: KClass<*>): EntityConfiguration =
        configurationMap[entityClass] ?: error("Configuration is not defined for $entityClass")


}