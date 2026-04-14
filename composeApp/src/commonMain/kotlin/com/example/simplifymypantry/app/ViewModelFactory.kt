package com.example.simplifymypantry.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.simplifymypantry.account.data.*
import com.example.simplifymypantry.account.presentation.*
import kotlin.reflect.KClass

class AppViewModelFactory(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val editUserUseCase: EditUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras
    ): T {
        return when (modelClass) {

            LoginViewModel::class ->
                LoginViewModel(loginUserUseCase) as T

            CreateAccountViewModel::class ->
                CreateAccountViewModel(registerUserUseCase, sessionManager) as T

            ViewAccountViewModel::class ->
                ViewAccountViewModel(
                    deleteUserUseCase,
                    editUserUseCase,
                    getUserUseCase,
                    sessionManager
                ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}