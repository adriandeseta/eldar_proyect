package com.example.eldar_proyect

import UserInfo
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.eldar_proyect.databinding.ActivityAddCardBinding

class AddCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityAddCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración del TextWatcher para formatear el número de tarjeta
        binding.addCardUserNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Evitar bucles infinitos
                binding.addCardUserNumber.removeTextChangedListener(this)

                // Obtener el texto actual sin espacios
                val originalText = s.toString().replace(" ", "")
                val formattedText = StringBuilder()

                // Insertar espacios cada cuatro dígitos
                for (i in originalText.indices) {
                    if (i > 0 && i % 4 == 0) {
                        formattedText.append(" ")
                    }
                    formattedText.append(originalText[i])
                }

                // Actualizar el campo de texto con el formato correcto
                binding.addCardUserNumber.setText(formattedText.toString())
                binding.addCardUserNumber.setSelection(formattedText.length)

                // Volver a agregar el listener
                binding.addCardUserNumber.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Configuración del botón para agregar una nueva tarjeta
        binding.btnAddNewCard.setOnClickListener {
            // Verificar que todos los campos estén completos
            if (binding.addCardUserName.text.isNullOrEmpty() ||
                binding.addCardUserSurname.text.isNullOrEmpty() ||
                binding.addCardUserNumber.text.isNullOrEmpty()) {

                Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Obtener los datos ingresados por el usuario
                val userName = binding.addCardUserName.text.toString()
                val userSurname = binding.addCardUserSurname.text.toString()
                val userCardNumber = binding.addCardUserNumber.text.toString().replace(" ", "")

                // Crear un objeto UserInfo
                val userInfo = UserInfo(userName, userSurname, userCardNumber)

                // Configurar el Intent para devolver el resultado
                val resultIntent = Intent().apply {
                    putExtra("userInfo", userInfo)
                }

                // Establecer el resultado de la actividad
                setResult(Activity.RESULT_OK, resultIntent)

                // Finalizar la actividad y devolver el resultado a com.example.eldar_proyect.HomeActivity
                finish()
            }
        }
    }
}
