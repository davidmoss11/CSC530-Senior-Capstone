package com.example.simplifymypantry.account.data


import com.example.simplifymypantry.account.domain.ReturnedAccount
import co.touchlab.kermit.Logger


val log = Logger.withTag("UserUseCases")

class RegisterUserUseCase(
    private val repository : AccountRepository
){
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ) : Result<ReturnedAccount>{
        log.d("Register Use Case invoked")
        if (username.isBlank() || username.length < 5){
            return Result.failure(
                Exception("Username must be longer than 4 characters"))
        }
        if (!email.contains("@" ) || !email.contains(".")) {
            return Result.failure(
                Exception("Not a valid email"))
        }

        val passwordRegex = Regex(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$"
        )

        if (!passwordRegex.matches(password)) {
            return Result.failure(
                Exception("Password must be 8+ chars with upper, lower, number, and special char")
            )
        }

        return repository.createUser(username, email, password)

    }
}

class EditUserUseCase(
    private val repository : AccountRepository
){
    suspend operator fun invoke(
        token: String,
        username: String?,
        email: String?,
        password: String?
    ) : Result<ReturnedAccount> {

        if (!token.startsWith("Bearer ")) {
            return Result.failure(Exception("Invalid token type"))
        }
        if (token.isBlank()) {
            return Result.failure(Exception("Token is missing"))
        }

        if (username != null) {
            if (username.isBlank() || username.length < 4) {
                return Result.failure(
                    Exception("Username must be longer than 4 characters")
                )
            }
        }
        if (email != null) {
            if (!email.contains("@") || !email.contains(".")) {
                return Result.failure(
                    Exception("Not a valid email")
                )
            }
        }
        if(password != null) {
            val passwordRegex = Regex(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$"
            )

            if (!passwordRegex.matches(password)) {
                return Result.failure(
                    Exception("Password must be 8+ chars with upper, lower, number, and special char")
                )
            }
        }
        return repository.editUser(token, username, email, password)
    }
}

class GetUserUseCase(
    private val repository : AccountRepository
) {
    suspend operator fun invoke(
        token: String
    ) : Result<ReturnedAccount> {
        if (!token.startsWith("Bearer ")) {
            return Result.failure(Exception("Invalid token type"))
        }
        if (token.isBlank()) {
            return Result.failure(Exception("Token is missing"))
        }

        return repository.getUser(token)
    }
}

class DeleteUserUseCase(
    private val repository : AccountRepository
) {
    suspend operator fun invoke(
        token: String
    ) : Result<String> {
        if (!token.startsWith("Bearer ")) {
            return Result.failure(Exception("Invalid token type"))
        }
        if (token.isBlank()) {
            return Result.failure(Exception("Token is missing"))
        }

        return repository.deleteUser(token)
    }
}

class LoginUserUseCase(
    private val repository : AccountRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ) : Result<ReturnedAccount> {
        return repository.loginUser(username, email, password)
    }
}

