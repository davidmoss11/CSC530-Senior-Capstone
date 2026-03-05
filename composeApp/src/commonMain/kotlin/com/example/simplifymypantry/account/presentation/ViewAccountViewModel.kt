package com.example.simplifymypantry.account.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class ViewAccountViewModel : ViewModel() {
    var userName by mutableStateOf("")
    var email by mutableStateOf("")
    var name by mutableStateOf("")
}