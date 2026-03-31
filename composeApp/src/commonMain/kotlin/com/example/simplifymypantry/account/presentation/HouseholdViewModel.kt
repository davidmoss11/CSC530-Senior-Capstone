package com.example.simplifymypantry.account.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HouseholdViewModel : ViewModel() {
    var inviteEmail by mutableStateOf("")

    val members = mutableListOf<String>()

    fun inviteUser() {
        if (inviteEmail.isNotBlank()) {
            inviteEmail = ""
        }
    }

    fun removeMember(member: String) {
        members.remove(member)
    }
}