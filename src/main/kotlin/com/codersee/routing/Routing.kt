package com.codersee.routing

import com.codersee.service.JwtService
import com.codersee.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.itemRouting(
  jwtService: JwtService,
  userService: UserService
) {
  routing {
    route("/api/item") {
      dataBaseItemRouting(userService)
    }

    route("/api/auth") {
      authRoute(jwtService)
    }

    route("/api/user") {
      userRoute(userService)
    }

  }
}