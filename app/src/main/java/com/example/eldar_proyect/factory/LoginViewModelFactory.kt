package com.example.eldar_proyect.factory

import com.example.eldar_proyect.viewModel.LoginViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eldar_proyect.data.DataBaseHelper

class LoginViewModelFactory(private val dataBaseHelper: DataBaseHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(dataBaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
