package com.codersee.dataBase


import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.sql.Connection
import java.sql.DriverManager

object MyDatabase {
    private lateinit var connection: Connection
    private lateinit var dslContext: DSLContext

    fun connect() {
        // Замените URL, имя пользователя и пароль на ваши актуальные данные
        val url = "jdbc:postgresql://dpg-ctmibk5umphs73dfijhg-a:5432/my_ktor_db"
        val user = "denis"
        val password = "fORtJznZxAap4k6rXpMICk9jA6fyaYDW"

        connection = DriverManager.getConnection(url, user, password)
        dslContext = DSL.using(connection, SQLDialect.POSTGRES)
    }

    fun getContext(): DSLContext {
        return dslContext
    }

    fun close() {
        connection.close()
    }
}