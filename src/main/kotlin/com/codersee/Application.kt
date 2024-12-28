package com.codersee

import com.codersee.dataBase.MyDatabase
import com.codersee.plugins.configureSecurity
import com.codersee.plugins.configureSerialization
import com.codersee.repository.UserRepository
import com.codersee.routing.itemRouting
import com.codersee.service.JwtService
import com.codersee.service.UserService
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.flywaydb.core.Flyway

fun main(args: Array<String>) {
    val flyway = Flyway.configure()
        .dataSource(
            "jdbc:postgresql://ep-summer-hat-a2pvv7w8.eu-central-1.aws.neon.tech/ktor_db?sslmode=require",
            "ktor_db_owner",
            "yX2C4ugGZWMn"
        )
        .load()
    flyway.migrate()
    MyDatabase.connect()
    EngineMain.main(args)

}

fun Application.module() {
    val userRepository = UserRepository()
    val userService = UserService(userRepository)
    val jwtService = JwtService(this, userService)
    configureSerialization()
    configureSecurity(jwtService)
    itemRouting(jwtService, userService)
}