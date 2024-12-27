package com.codersee.routing

import com.codersee.dataBase.MyDatabase
import com.codersee.util.Item
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jooq.impl.DSL

fun Application.dataBaseItemRouting() {
    routing {
        route("api/items") {
            get {
                val items = MyDatabase.getContext().select().from("items").fetch().into(Item::class.java)
                call.respond(items)
            }

            post {
                val item = call.receive<Item>()
                val id = MyDatabase.getContext().insertInto(DSL.table("items"))
                    .set(DSL.field("name"), item.name)
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
    }
}
