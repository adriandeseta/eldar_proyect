package com.example.eldar_proyect

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eldar_proyect.data.UserData
import com.example.eldar_proyect.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var arrayListUsers = ArrayList<HashMap<String, String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainTitle.text = "ELDAR"
        binding.user.hint = "Usuario"
        binding.password.hint = "Contraseña"
        binding.btnSubmit.text = "Ingresar"

        binding.btnSubmit.setOnClickListener {
            if(binding.user.text.isEmpty()){
                Toast.makeText(this, "el usuario no puede estar vacio", Toast.LENGTH_SHORT).show()
            }
            if (binding.password.text.isEmpty()){
                Toast.makeText(this, "el password no puede estar vacio", Toast.LENGTH_SHORT).show()
            } else{
                val user = binding.user.text.toString()
                val password = binding.password.text.toString()
                val userData = UserData(this)
                getUserData()
               if(checkUserCredentials(user, password)) {
                   Toast.makeText(this, "EL USUARIO Y CONTRASEÑA SON CORRECTOS", Toast.LENGTH_SHORT).show()
               } else{
                   Toast.makeText(this, "EL USUARIO Y CONTRASEÑA SON INCORRECTOS", Toast.LENGTH_SHORT).show()

               }

                val result = userData.userRegister(userData, user, password)
                Toast.makeText(this, "usuario guardado$result", Toast.LENGTH_SHORT).show()
                binding.user.setText("")
                binding.password.setText("")

            }

        }

    }

    private fun getUserData() {
        val userData = UserData(this)
        val cursor = userData.showData(userData)
        cursor?.let {
            if (cursor.moveToFirst()){
                do {
                    val map:HashMap<String, String> = HashMap()
                    map.put("idUser", cursor.getString(cursor.getColumnIndexOrThrow("idUser")))
                    map.put("user", cursor.getString(cursor.getColumnIndexOrThrow("user")))
                    map.put("password", cursor.getString(cursor.getColumnIndexOrThrow("password")))
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