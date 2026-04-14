package com.example.simplifymypantry.account.data

import com.example.simplifymypantry.account.domain.IAccountRepository
import com.example.simplifymypantry.account.domain.ReturnedAccount
import co.touchlab.kermit.Logger

class AccountRepository(
    private val apiService : AccountApiService
): IAccountRepository {

    val log = Logger.withTag("AccountRepository")
     override suspend fun getUser(
         token: String
     ) : Result<ReturnedAccount>{
        return try {
            val response = apiService.getUser(token)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun createUser(
        username: String,
        email: String,
        password: String
    ) : Result<ReturnedAccount>{
        log.d("Repository Received")
        return try {
            val response = apiService.registerUser(username, email, password)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun editUser(
        token: String,
        username: String?,
        email: String?,
        password: String?
    ) : Result<ReturnedAccount>{
        return try {
            val response = apiService.editUser(token, username, email, password)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(
        token: String
    ) : Result<String>{
        return try {
            apiService.deleteUser(token)
            Result.success("User deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginUser(
        username: String,
        email: String,
        password: String
    ) : Result<ReturnedAccount>{
            return try {
                val response = apiService.loginUser(username, email, password)
                Result.success(response.toDomain())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

}
