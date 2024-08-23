package com.example.eldar_proyect

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eldar_proyect.data.UserData
import com.example.eldar_proyect.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var arrayListUsers = ArrayList<HashMap<String, String>>()
    private var dataLoaded = false  // Bandera para saber si ya se han cargado los datos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainTitle.text = "ELDAR"
        binding.user.hint = "Ingrese usuario"
        binding.password.hint = "Ingrese Contraseña"
        binding.btnSubmit.text = "Log in"

        binding.btnSubmit.setOnClickListener {
            if (binding.user.text.isEmpty() || binding.password.text.isEmpty()) {
                Toast.makeText(this, "Debe completar ambos campos", Toast.LENGTH_SHORT).show()
            } else {
                val user = binding.user.text.toString()
                val password = binding.password.text.toString()

                // Cargamos los datos desde la base de datos si aún no lo hemos hecho
                if (!dataLoaded) {
                    getUserData()
                    dataLoaded = true
                }

                // Verificamos si las credenciales son correctas
                if (checkUserCredentials(user, password)) {
                    Toast.makeText(this, "EL USUARIO Y CONTRASEÑA SON CORRECTOS", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    // Si no coinciden, registramos el nuevo usuario y contraseña
                    val userData = UserData(this)
                    userData.userRegister(userData, user, password)
                    // Después de registrar, actualizamos la lista de usuarios
                    arrayListUsers.clear()  // Limpiamos la lista actual para evitar duplicados
                    getUserData()
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                }

                // Limpiamos los campos
                binding.user.setText("")
                binding.password.setText("")
            }
        }
    }

    private fun getUserData() {
        val userData = UserData(this)
        val cursor = userData.showData(userData)
        cursor.let {
            if (cursor.moveToFirst()) {
                do {
                    val map: HashMap<String, String> = HashMap()
                    map["idUser"] = cursor.getString(cursor.getColumnIndexOrThrow("idUser"))
                    map["user"] = cursor.getString(cursor.getColumnIndexOrThrow("user"))
                    map["password"] = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                    arrayListUsers.add(map)
                } while (cursor.moveToNext())
            }
        }
    }

    private fun checkUserCredentials(user: String, password: String): Boolean {
        for (map in arrayListUsers) {
            val storedUser = map["user"]
            val storedPassword = map["password"]

            // Verificamos si tanto el usuario como la contraseña coinciden
            if (storedUser == user && storedPassword == password) {
                return true // Credenciales correctas
            }
        }
        return false // No se encontró una coincidencia
    }
}
