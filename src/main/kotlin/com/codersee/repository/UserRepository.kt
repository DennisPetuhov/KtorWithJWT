package com.codersee.repository

import com.codersee.dataBase.MyDatabase
import com.codersee.model.User
import org.jooq.impl.DSL

class UserRepository {
    val myDataBaseContext = MyDatabase.getContext()

    fun findAll(): List<User> =
        myDataBaseContext.select().from("users").fetch().into(User::class.java)

    fun findById(id: Int): User? =
        myDataBaseContext.select().from("users")
            .where(DSL.field("id").eq(id))
            .fetchOne()
            ?.into(User::class.java)

    fun findByUsername(username: String): User? =
        myDataBaseContext.select().from("public.users")
            .where(DSL.field("username").eq(username))
            .fetchOne()
            ?.into(User::class.java)

    fun save(user: User): Boolean =
        myDataBaseContext.insertInto(DSL.table("users"))
            .set(DSL.field("id"), user.id)
            .set(DSL.field("username"), user.username)
            .set(DSL.field("password"), user.password)
            .execute() > 0
}