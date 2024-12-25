val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.20"
    id("io.ktor.plugin") version "2.3.6"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20"
}

group = "com.codersee"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    // Устанавливаем JVM-параметры для режима разработки
    val isDevelopment: Boolean = project.hasProperty("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

tasks {
    val fatJar = register<Jar>("fatJar") {
        group = "build"
        description = "Assembles a fat JAR file containing all runtime dependencies."

        // Зависимости задачи
        dependsOn("compileJava", "compileKotlin", "processResources")

        // Имя файла JAR
        archiveClassifier.set("standalone")

        // Исключаем дубликаты (например, META-INF)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        // Добавляем классы и ресурсы
        from(sourceSets.main.get().output)

        // Включаем зависимости
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
       manifest { attributes["Main-Class"] = "com.codersee.ApplicationKt" }
    }


    // Добавляем задачу fatJar как зависимость от сборки
    named("build") {
        dependsOn(fatJar)
    }
}