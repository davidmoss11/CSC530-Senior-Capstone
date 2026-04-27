package com.example.simplifymypantry.account.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.simplifymypantry.account.data.LoginUserUseCase
import kotlinx.coroutines.launch
import co.touchlab.kermit.Logger
import com.example.simplifymypantry.account.data.SessionManager

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    val log = Logger.withTag("LoginViewModel")

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var showDialog by mutableStateOf(false)
    var dialogMessage by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)

    fun loginButtonClicked() {
        log.d("loginButtonClicked")
        if(username == "" || password == ""){
            showDialog = true
            dialogMessage = "Missing Username or Password"
        }

        viewModelScope.launch{
            loginUserUseCase.invoke(username, username, password) //username twice because they can enter either
                .onSuccess{ user ->
                    sessionManager.saveSession(
                        id = user.id,
                        username = user.username,
                        email = user.email,
                        token = user.token
                    )
                    isLoggedIn = true
                    dialogMessage = "Successfully Logged In"
                    showDialog = true
                }
                .onFailure{ error ->
                    error.message?.let { log.d(it) }
                    dialogMessage = "Failed to Login"
                    showDialog = true
                }
        }
    }

}