package com.example.simplifymypantry.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.example.simplifymypantry.account.data.RegisterUserUseCase
import com.example.simplifymypantry.account.data.SessionManager
import kotlinx.coroutines.launch


class CreateAccountViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var showDialog by mutableStateOf(false)
    var dialogMessage by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var isSuccessful by mutableStateOf(false)

    fun createAccountClicked(){
        print("Create Account View Model Function Activated")
        if (password != confirmPassword) {
            dialogMessage = "Passwords do not match"
            showDialog = true
            return
        }

        viewModelScope.launch{
            isLoading = true

            registerUserUseCase.invoke(username, email, password)
                .onSuccess{ user ->
                    sessionManager.saveSession(
                        id = user.id,
                        username = user.username,
                        email = user.email,
                        token = user.token //temporary, will need to encrypt in the future
                    )
                    dialogMessage = "Account Creation Successful"
                    showDialog = true
                    isSuccessful = true
                    //go to home screen
                }
                .onFailure { error ->
                    dialogMessage = error.message ?: "Registration failed"
                    showDialog = true
                }

            isLoading = false
        }
    }
}