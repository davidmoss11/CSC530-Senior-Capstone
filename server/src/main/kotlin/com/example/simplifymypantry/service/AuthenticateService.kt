package com.example.simplifymypantry.service

import com.example.simplifymypantry.models.RegisterUser
import com.example.simplifymypantry.models.User
import com.example.simplifymypantry.models.UpdateUser
import org.mindrot.jbcrypt.BCrypt
import com.example.simplifymypantry.database.IDatabaseFactory
import com.example.simplifymypantry.models.LoginUser
import com.example.simplifymypantry.models.Users
import com.example.simplifymypantry.util.LoginFailed
import com.example.simplifymypantry.util.UserDoesNotExist
import com.example.simplifymypantry.util.UserExists
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or
import java.util.UUID

interface IAuthService{ //interface allows for future adding or swapping out dbs without having to modify routes

    suspend fun createUser(request: RegisterUser) : User

    suspend fun getUser(request: LoginUser) : User

    suspend fun editUser(userId: String, request: UpdateUser) : User

    suspend fun deleteUser(userId: String)

    suspend fun getUserById(id: String) : User



}

class AuthService(private val databaseFactory: IDatabaseFactory): IAuthService { //current class to query our database

    override suspend fun createUser(request: RegisterUser): User {

        println("Auth Service Create User activated")

        return databaseFactory.dbQuery{

            val userInDatabase = User.find{
                (Users.username eq request.user.username) or (Users.email eq request.user.email)
            }.firstOrNull()
            if (userInDatabase != null) throw UserExists()

            val hashedPass = BCrypt.hashpw(request.user.password, BCrypt.gensalt())
            User.new {
                username = request.user.username
                email = request.user.email
                hashedPassword = hashedPass
            }
        }
    }

    override suspend fun getUser(request: LoginUser): User {

        val userInDatabase = databaseFactory.dbQuery{
            User.find{
                (Users.username eq request.user.username) or (Users.email eq request.user.email)
            }.firstOrNull()
        }

        if (userInDatabase == null) throw UserDoesNotExist()

        else {
            if(BCrypt.checkpw(request.user.password, userInDatabase.hashedPassword)) return userInDatabase
            else throw LoginFailed()
        }


    }

    override suspend fun editUser(userId: String, request: UpdateUser): User {
        return databaseFactory.dbQuery {
            val user = findUserById(userId)
            val hashedPass = BCrypt.hashpw(request.user.password, BCrypt.gensalt())
            user.apply{
                email = request.user.email ?: email
                username = request.user.username ?: username
                hashedPassword = hashedPass ?: hashedPassword

            }
        }
    }

    override suspend fun deleteUser(userId: String){
        return databaseFactory.dbQuery {
            val user = findUserById(userId)
            user.delete()
        }
        
    }

    override suspend fun getUserById(id: String): User = databaseFactory.dbQuery {
        findUserById(id)
    }

    private fun findUserById(id: String): User {
        return User.findById(UUID.fromString(id)) ?: throw UserDoesNotExist()
    }

}