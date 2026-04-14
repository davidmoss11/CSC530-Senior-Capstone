package com.example.simplifymypantry.account.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplifymypantry.account.data.DeleteUserUseCase
import com.example.simplifymypantry.account.data.EditUserUseCase
import com.example.simplifymypantry.account.data.GetUserUseCase
import com.example.simplifymypantry.account.data.SessionManager
import kotlinx.coroutines.launch


class ViewAccountViewModel(
    private val deleteUserUseCase : DeleteUserUseCase,
    private val editUserUseCase : EditUserUseCase,
    private val getUserUseCase : GetUserUseCase,
    private val sessionManager : SessionManager
): ViewModel() {
    var username by mutableStateOf<String?>(null)
    var email by mutableStateOf<String?>(null)
    var password by mutableStateOf("")
    var token by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
    var isCheckComplete by mutableStateOf(false)
    var showEditDialog by mutableStateOf(false)
    var editDialogType by mutableStateOf(EditType.USERNAME)
    var editDialogValue by mutableStateOf("")
    enum class EditType {
        USERNAME, EMAIL, PASSWORD
    }

    fun checkLoginStatus() {
        val session = sessionManager.getSession()
        if (session != null){
            username = session.username
            email = session.email
            token = session.token
            isLoggedIn = true
            isCheckComplete = true
        }
        else{
            getUser()
        }
    }

    fun editUsername() {
        editDialogType = EditType.USERNAME
        editDialogValue = username ?: ""
        showEditDialog = true
    }

    fun editEmail() {
        editDialogType = EditType.EMAIL
        editDialogValue = email ?: ""
        showEditDialog = true
    }

    fun editPassword() {
        editDialogType = EditType.PASSWORD
        editDialogValue = ""
        showEditDialog = true
    }

    fun deleteAccount(){

        viewModelScope.launch{
            deleteUserUseCase.invoke(token)
                .onSuccess {
                    isLoggedIn = false
                    //popup
                }
                .onFailure {
                    //popup
                }
        }
    }

    fun confirmEdit() {
        when (editDialogType) {
            EditType.USERNAME -> username = editDialogValue
            EditType.EMAIL -> email = editDialogValue
            EditType.PASSWORD -> password = editDialogValue
        }

        editAccount()
    }

    fun editAccount(){

        viewModelScope.launch{
            editUserUseCase.invoke(token, username, email, password)
                .onSuccess { user ->
                    sessionManager.clear()
                    sessionManager.saveSession(
                        id = user.id,
                        username = user.username,
                        email = user.email,
                        token = user.token
                    )
                    isLoggedIn = true
                    isCheckComplete = true
                    password = ""
                }
                .onFailure{
                    isLoggedIn = false
                    isCheckComplete = true
                }
        }
    }

    fun getUser(){

        viewModelScope.launch{
            getUserUseCase.invoke(token)
                .onSuccess { user ->
                    sessionManager.clear()
                    sessionManager.saveSession(
                        id = user.id,
                        username = user.username,
                        email = user.email,
                        token = user.token
                    )
                    isLoggedIn = true
                    password = ""
                }
                .onFailure{

                }
        }
    }

    fun logoutUser(){
        sessionManager.clear()
        isLoggedIn = false
    }
}