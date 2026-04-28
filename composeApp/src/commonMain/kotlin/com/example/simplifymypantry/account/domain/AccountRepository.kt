package com.example.simplifymypantry.account.domain

interface IAccountRepository{

    suspend fun getUser(token: String) : Result<ReturnedAccount>

    suspend fun createUser(username: String, email: String, password: String) : Result<ReturnedAccount>

    suspend fun editUser(token: String, username: String?, email: String?, password: String?) : Result<ReturnedAccount>

    suspend fun deleteUser(token: String) : Result<String>

    suspend fun loginUser(username: String, email: String, password: String) : Result<ReturnedAccount>
}