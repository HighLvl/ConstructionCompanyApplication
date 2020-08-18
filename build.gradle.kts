import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.openjfx.javafxplugin") version "0.0.9"
    kotlin("jvm") version "1.3.72"
}
group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.11.2")
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("no.tornado:tornadofx-controls:1.0.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")
    implementation("org.springframework.hateoas:spring-hateoas:1.1.1.RELEASE")
    implementation("org.springframework.data:spring-data-commons:2.3.2.RELEASE")

}
tasks.withType<Test> {
    useJUnitPlatform()
}
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }

    javafx {
        modules("javafx.controls", "javafx.fxml", "javafx.graphics")
    }
}
