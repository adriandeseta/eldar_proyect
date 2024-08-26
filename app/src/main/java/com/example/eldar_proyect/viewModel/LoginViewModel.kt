package com.example.eldar_proyect.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eldar_proyect.data.DataBaseHelper

class LoginViewModel(private val dataBaseHelper: DataBaseHelper) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun login(user: String, password: String) {
        if (user.isEmpty() || password.isEmpty()) {
            _loginState.value = LoginState.Error("Debe completar ambos campos")
            return
        }

        val cursor = dataBaseHelper.getAllUsers()
        var userId: Int = -1
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("idUser"))
                val storedUser = cursor.getString(cursor.getColumnIndexOrThrow("user"))
                val storedPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"))

                if (storedUser == user && storedPassword == password) {
                    userId = id
                    break
                }
            } while (cursor.moveToNext())
        }

        if (userId != -1) {
            _loginState.value = LoginState.Success(userId)
        } else {
            dataBaseHelper.userRegister(user, password)
            _loginState.value = LoginState.Registered
        }
    }
}

sealed class LoginState {
    data class Success(val userId: Int) : LoginState()
    data class Error(val message: String) : LoginState()
    data object Registered : LoginState()
}
