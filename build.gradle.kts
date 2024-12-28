val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val jooq_version: String by project
val kotlin_coroutines_version: String by project
val postgresql_r2dbc: String by project
val postgresql_jdbc: String by project

plugins {
    kotlin("jvm") version "1.9.20"
    id("io.ktor.plugin") version "2.3.6"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20"
    id("nu.studer.jooq") version "8.1"
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

//configurations {
//    create("jooqGenerator")
//}

dependencies {
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("org.postgresql:postgresql:42.7.4")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("org.jooq:jooq:$jooq_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$kotlin_coroutines_version")
    implementation("io.r2dbc:r2dbc-postgresql:$postgresql_r2dbc")
    implementation("org.flywaydb:flyway-core:9.1.6")
//    jooqGenerator("org.postgresql:postgresql:$postgresql_jdbc")
}

//jooq {
//    version.set(jooq_version)
//    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)
//
//    configurations {
//        create("main") {
//            generateSchemaSourceOnCompilation.set(false)
//            generationTool(closureOf<org.jooq.meta.jaxb.Configuration> {
//                logging = org.jooq.meta.jaxb.Logging.WARN
//                jdbc
//                jdbc {
//                    driver = "org.postgresql.Driver"
//                    url = "jdbc:postgresql://localhost:5432/yourdatabase"
//                    user = "yourusername"
//                    password = "yourpassword"
//                }
////                generator {
//                    name = "org.jooq.codegen.KotlinGenerator"
//                    database {
//                        inputSchema = "public"
//                    }
//                    generate {
//                        deprecated = false
//                        records = true
//                        immutablePojos = false
//                        fluentSetters = false
//                        daos = false
//                    }
//                    target {
//                        packageName = "com.codersee.generated"
//                        directory = "src/generated/kotlin"
//                    }
//                }
//            })
//        }
//    }
//}

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