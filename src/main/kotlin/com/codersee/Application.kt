package com.codersee

import com.codersee.dataBase.MyDatabase
import com.codersee.plugins.configureSecurity
import com.codersee.plugins.configureSerialization
import com.codersee.repository.UserRepository
import com.codersee.routing.dataBaseItemRouting
import com.codersee.routing.itemRouting
import com.codersee.service.JwtService
import com.codersee.service.UserService
import io.ktor.server.application.*
import org.flywaydb.core.Flyway

fun main(args: Array<String>) {
    val flyway = Flyway.configure()
        .dataSource(
            "jdbc:postgresql://dpg-ctmibk5umphs73dfijhg-a:5432/my_ktor_db",
            "denis",
            "fORtJznZxAap4k6rXpMICk9jA6fyaYDW"
        )
        .load()
    flyway.migrate()
    MyDatabase.connect()
    io.ktor.server.netty.EngineMain.main(args)

}

fun Application.module() {
    val userRepository = UserRepository()
    val userService = UserService(userRepository)
    val jwtService = JwtService(this, userService)

    configureSerialization()
    configureSecurity(jwtService)
    itemRouting(jwtService, userService)
    dataBaseItemRouting()
}