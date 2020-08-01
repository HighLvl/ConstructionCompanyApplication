package com.example.ConstructionCompanyApplication

import com.example.ConstructionCompanyApplication.dto.MachineryModel
import com.example.ConstructionCompanyApplication.ui.view.MachineryModelView
import javafx.application.Application
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import tornadofx.App
import tornadofx.DIContainer
import tornadofx.FX
import kotlin.reflect.KClass

@SpringBootApplication
class TornadoFxApplication : App(MachineryModelView::class) { //The application class must be a TornadoFX application and it must have the main view

    private lateinit var context: ConfigurableApplicationContext //We are going to set application context here

    override fun init() {
        this.context = SpringApplication.run(this.javaClass) //We start the application context and let Spring Boot to initilaize itself
        context.autowireCapableBeanFactory.autowireBean(this) //We ask the context to inject all needed dependencies into the current instence (if needed)

        FX.dicontainer = object : DIContainer { // Here we have to implement an interface for TornadoFX DI support
            override fun <T : Any> getInstance(type: KClass<T>): T = context.getBean(type.java) // We find dependencies directly in Spring's application context
            override fun <T : Any> getInstance(type: KClass<T>, name: String): T = context.getBean(name, type.java)
        }
    }

    override fun stop() { // On stop, we have to stop spring as well
        super.stop()
        context.close()
    }

}

fun main(args: Array<String>) {
    Application.launch(TornadoFxApplication::class.java, *args) //Using JavaFX applicatin launcher
}