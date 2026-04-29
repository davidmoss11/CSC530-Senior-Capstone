package com.example.simplifymypantry.account.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplifymypantry.account.data.SessionManager
import com.example.simplifymypantry.account.data.DeleteUserUseCase
import com.example.simplifymypantry.account.data.EditUserUseCase
import com.example.simplifymypantry.account.data.GetUserUseCase
import kotlinx.coroutines.launch


class ViewAccountViewModel(
    private val deleteUserUseCase: DeleteUserUseCase,
    private val editUserUseCase: EditUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    var userName by mutableStateOf(sessionManager.getUsername() ?: "")
    var email by mutableStateOf(sessionManager.getEmail() ?: "")
    var name by mutableStateOf("")
    var isLoggedIn by mutableStateOf(sessionManager.getSession() != null)
    var pushNotificationsEnabled by mutableStateOf(false)
    
    var showDialog by mutableStateOf(false)
    var dialogMessage by mutableStateOf("")

    fun logout() {
        sessionManager.clear()
        isLoggedIn = false
    }

    fun deleteAccount(onDeleted: () -> Unit) {
        val token = sessionManager.getToken() ?: return
        viewModelScope.launch {
            val result = deleteUserUseCase.invoke(token)
            if (result.isSuccess) {
                logout()
                onDeleted()
            } else {
                dialogMessage = "Failed to delete account."
                showDialog = true
            }
        }
    }

    fun updateUsername(newUsername: String) {
        val token = sessionManager.getToken() ?: return
        viewModelScope.launch {
            val result = editUserUseCase.invoke(token, newUsername, null, null)
            if (result.isSuccess) {
                userName = newUsername
                dialogMessage = "Username updated successfully!"
                showDialog = true
            }
        }
    }

    fun updateEmail(newEmail: String) {
        val token = sessionManager.getToken() ?: return
        viewModelScope.launch {
            val result = editUserUseCase.invoke(token, null, newEmail, null)
            if (result.isSuccess) {
                email = newEmail
                dialogMessage = "Email updated successfully!"
                showDialog = true
            }
        }
    }
    
    fun updatePassword(newPassword: String) {
        val token = sessionManager.getToken() ?: return
        viewModelScope.launch {
            val result = editUserUseCase.invoke(token, null, null, newPassword)
            if (result.isSuccess) {
                dialogMessage = "Password updated successfully!"
                showDialog = true
            }
        }
    }
}