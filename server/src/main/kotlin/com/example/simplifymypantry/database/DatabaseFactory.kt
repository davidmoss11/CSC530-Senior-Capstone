package com.example.simplifymypantry.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import com.example.simplifymypantry.config.*
import com.example.simplifymypantry.models.Users
import kotlinx.coroutines.withContext

interface IDatabaseFactory{

    fun init()

    fun drop()

    suspend fun <T> dbQuery(block: () -> T): T
}


class DatabaseFactory(private val environment: ApplicationEnvironment) {

    //connects to the database
    fun init(){
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(Users)
        }
    }

    //configures Hikari database connection pool to use info from application.conf file
    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = environment.config.property("database.driver").getString()
        config.jdbcUrl = environment.config.property("database.url").getString()
        config.username = environment.config.property("database.username").getString()
        config.password = environment.config.property("database.password").getString()
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    fun drop(){
        transaction{
            SchemaUtils.drop(Users)
        }
    }

    suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }

}