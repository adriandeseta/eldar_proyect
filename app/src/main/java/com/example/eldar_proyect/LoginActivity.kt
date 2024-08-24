package com.example.eldar_proyect

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.eldar_proyect.data.DataBaseHelper
import com.example.eldar_proyect.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var dataLoaded = false
    private lateinit var dataBaseHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.mainTitle.text = "ELDAR"
        binding.user.hint = "Ingrese usuario"
        binding.password.hint = "Ingrese Contraseña"
        binding.btnSubmit.text = "Sign up"

        dataBaseHelper = DataBaseHelper(this)

        binding.btnSubmit.setOnClickListener {
            val user = binding.user.text.toString()
            val password = binding.password.text.toString()

            if (user.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Debe completar ambos campos", Toast.LENGTH_SHORT).show()
            } else {
                if (!dataLoaded) {
                    dataLoaded = true
                    binding.btnSubmit.text = "Log in"
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
                    Toast.makeText(this, "EL USUARIO Y CONTRASEÑA SON CORRECTOS", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                } else {
                    // Registrar nuevo usuario
                    dataBaseHelper.userRegister(user, password)
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                    binding.user.setText("")
                    binding.password.setText("")
                }
            }
        }
    }
}
