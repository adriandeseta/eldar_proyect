package com.example.eldar_proyect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.eldar_proyect.data.DataBaseHelper
import com.example.eldar_proyect.databinding.ActivityAddCardBinding
import com.example.eldar_proyect.dto.UserInfo

class AddCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCardBinding
    private lateinit var dataBaseHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityAddCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBaseHelper = DataBaseHelper(this)

        // Obtener el ID del usuario desde el Intent
        val userId = intent.getIntExtra("userId", -1)

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

        binding.btnAddNewCard.setOnClickListener {
            val userName = binding.addCardUserName.text.toString()
            val userSurname = binding.addCardUserSurname.text.toString()
            val userCardNumber = binding.addCardUserNumber.text.toString().replace(" ", "")
            // Determinar el tipo de tarjeta
            val cardType = when {
                userCardNumber.startsWith("3") -> "AMEX"
                userCardNumber.startsWith("4") -> "VISA"
                userCardNumber.startsWith("5") -> "MASTERCARD"
                else -> "Unknown"
            }

            // Verificar que todos los campos estén completos y que el ID del usuario sea válido
            if (userName.isNotEmpty() && userSurname.isNotEmpty() && userCardNumber.isNotEmpty()) {
                // Agregar la tarjeta a la base de datos
                dataBaseHelper.addCard(userId, userName, userSurname, userCardNumber, cardType)

                // Crear un objeto UserInfo con el ID del usuario
                val userInfo = UserInfo(
                    id = userId,
                    user = "", // Puede estar vacío ya que estamos en AddCardActivity
                    password = "", // También vacío
                    name = userName,
                    surname = userSurname,
                    cardNumber = userCardNumber,
                    cardType = cardType
                )

                // Crear un Intent de resultado con el objeto UserInfo
                val resultIntent = Intent().apply {
                    putExtra("userInfo", userInfo)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Cerrar esta actividad y volver a HomeActivity
            } else {
                // Mostrar un mensaje si los campos no están completos o el ID no es válido
                Toast.makeText(this, "Debe completar todos los campos y seleccionar un usuario válido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
