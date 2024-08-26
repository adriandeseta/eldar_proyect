package com.example.eldar_proyect

import com.example.eldar_proyect.viewModel.LoginState
import com.example.eldar_proyect.viewModel.LoginViewModel
import com.example.eldar_proyect.factory.LoginViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.example.eldar_proyect.data.DataBaseHelper
import com.example.eldar_proyect.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(DataBaseHelper(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        loginViewModel.loginState.observe(this, Observer { state ->
            when (state) {
                is LoginState.Success -> {
                    Toast.makeText(this, "EL USUARIO Y CONTRASEÑA SON CORRECTOS", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("userId", state.userId)
                    startActivity(intent)
                }
                is LoginState.Registered -> {
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                    binding.user.setText("")
                    binding.password.setText("")
                    binding.btnSubmit.text = getString(R.string.log_in_btn_label)
                }
                is LoginState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }

                else -> {
                    // Not Implemented
                }
            }
        })

        binding.btnSubmit.setOnClickListener {
            val user = binding.user.text.toString()
            val password = binding.password.text.toString()
            loginViewModel.login(user, password)
        }
    }
}
