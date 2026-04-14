package com.example.simplifymypantry.account.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.simplifymypantry.account.data.LoginUserUseCase
import kotlinx.coroutines.launch
import co.touchlab.kermit.Logger

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    val log = Logger.withTag("LoginViewModel")

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var showDialog by mutableStateOf(false)
    var dialogMessage by mutableStateOf("")

    fun loginButtonClicked() {
        log.d("loginButtonClicked")
        if(username == "" || password == ""){
            showDialog = true
            dialogMessage = "Missing Username or Password"
        }

        viewModelScope.launch{
            loginUserUseCase.invoke(username, username, password) //username twice because they can enter either
        }
    }

    fun createAccountButtonClicked() {

    }

    fun cornerXButtonClicked() {

    }

}