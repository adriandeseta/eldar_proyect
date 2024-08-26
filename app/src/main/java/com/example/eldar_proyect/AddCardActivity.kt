package com.example.eldar_proyect

import EncryptionHelper
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.example.eldar_proyect.data.DataBaseHelper
import com.example.eldar_proyect.databinding.ActivityAddCardBinding
import com.example.eldar_proyect.factory.AddCardViewModelFactory
import com.example.eldar_proyect.viewModel.AddCardViewModel

class AddCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCardBinding
    private val addCardViewModel: AddCardViewModel by viewModels {
        AddCardViewModelFactory(DataBaseHelper(this), EncryptionHelper())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityAddCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el ID del usuario desde el Intent
        val userId = intent.getIntExtra("userId", -1)

        binding.addCardUserNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.addCardUserNumber.removeTextChangedListener(this)

                val formattedText = addCardViewModel.formatCardNumber(s.toString())
                binding.addCardUserNumber.setText(formattedText)
                binding.addCardUserNumber.setSelection(formattedText.length)

                binding.addCardUserNumber.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnAddNewCard.setOnClickListener {
            val userName = binding.addCardUserName.text.toString()
            val userSurname = binding.addCardUserSurname.text.toString()
            val userCardNumber = binding.addCardUserNumber.text.toString().replace(" ", "")

            addCardViewModel.addNewCard(userId, userName, userSurname, userCardNumber)
        }

        // Observar cambios en userInfo y errorMessage
        addCardViewModel.userInfo.observe(this, Observer { userInfo ->
            userInfo?.let {
                val resultIntent = Intent().apply {
                    putExtra("userInfo", it)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        })

        addCardViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                addCardViewModel.clearErrorMessage()
            }
        })
    }
}
