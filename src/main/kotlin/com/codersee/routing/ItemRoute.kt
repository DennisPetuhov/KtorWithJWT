package com.codersee.routing

import com.codersee.dataBase.MyDatabase
import com.codersee.service.UserService
import com.codersee.util.Item
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jooq.impl.DSL

fun Route.dataBaseItemRouting(userService: UserService) {
    get {
        val items = MyDatabase.getContext().select().from("items").fetch().into(Item::class.java)
        call.respond(items)
    }
    get("/user") {
        val principal = call.principal<JWTPrincipal>()
        val userName =
            principal?.getClaim("username", String::class) ?: return@get call.respond(HttpStatusCode.Unauthorized)
        val user = userService.findByUsername(userName) ?: return@get call.respond(HttpStatusCode.NotFound)
        val items = getItemsByUserId(user.id!!)
        call.respond(items)
    }

    post {
        val principal = call.principal<JWTPrincipal>()
        val userName =
            principal?.getClaim("username", String::class) ?: return@post call.respond(HttpStatusCode.Unauthorized)
        val user = userService.findByUsername(userName) ?: return@post call.respond(HttpStatusCode.NotFound)

        val item = call.receive<Item>()
        val id = MyDatabase.getContext()
            .insertInto(DSL.table("items"))
            .set(DSL.field("name"), item.name)
            .set(DSL.field("user_id"), user.id)
            .returning(DSL.field("id"))
            .fetchOne()
            ?.getValue(DSL.field("id")) as? Int ?: throw Exception("Failed to insert item")
        call.respond(HttpStatusCode.Created, item.copy(id = id))
    }

    get("{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        val item = MyDatabase.getContext().select().from("items")
            .where(DSL.field("id").eq(id))
            .fetchOne()
            ?.into(Item::class.java)
        if (item != null) {
            call.respond(item)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    put("{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        val item = call.receive<Item>()
        val updated = MyDatabase.getContext().update(DSL.table("items"))
            .set(DSL.field("name"), item.name)
            .where(DSL.field("id").eq(id))
            .execute()
        if (updated > 0) {
            call.respond(HttpStatusCode.OK, item.copy(id = id!!))
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    delete("{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        val deleted = MyDatabase.getContext().deleteFrom(DSL.table("items"))
            .where(DSL.field("id").eq(id))
            .execute()
        if (deleted > 0) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}

fun getItemsByUserId(userId: Int): List<Item> {
    val items = MyDatabase.getContext().selectFrom(DSL.table("items")).where(DSL.field("user_id").eq(userId))
        .fetch().into(Item::class.java)
    return items
}