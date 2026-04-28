package com.example.simplifymypantry.account.data

import com.example.simplifymypantry.account.domain.ReturnedAccount

class SessionManager(
    private val db: AccountDatabase
) {

    fun saveSession(id: String, username: String, email: String, token: String) {
        db.accountDatabaseQueries.insertSession(
            id = id,
            username = username,
            email = email,
            token = token
        )
    }

    fun getSession(): ReturnedAccount? {
        return db.accountDatabaseQueries
            .selectSession()
            .executeAsOneOrNull()
            ?.let{
                ReturnedAccount(
                    id = it.id,
                    username = it.username,
                    email = it.email,
                    token = it.token
                )
            }
    }

    fun getUsername(): String? {
        return db.accountDatabaseQueries
            .selectSession()
            .executeAsOneOrNull()
            ?.username
    }

    fun getEmail(): String? {
        return db.accountDatabaseQueries
            .selectSession()
            .executeAsOneOrNull()
            ?.email
    }

    fun getToken(): String? {
        return db.accountDatabaseQueries
            .selectSession()
            .executeAsOneOrNull()
            ?.token
    }

    fun clear() {
        db.accountDatabaseQueries.clearSession()
    }
}