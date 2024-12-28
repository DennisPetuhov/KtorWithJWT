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
        val url = "jdbc:postgresql://ep-summer-hat-a2pvv7w8.eu-central-1.aws.neon.tech/ktor_db?sslmode=require"
        val user = "ktor_db_owner"
        val password = "yX2C4ugGZWMn"

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