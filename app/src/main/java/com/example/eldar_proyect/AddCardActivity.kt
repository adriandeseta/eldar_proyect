package com.example.eldar_proyect

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    }
}