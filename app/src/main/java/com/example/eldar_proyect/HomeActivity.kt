package com.example.eldar_proyect

import EncryptionHelper
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eldar_proyect.data.DataBaseHelper
import com.example.eldar_proyect.databinding.ActivityHomeBinding
import com.example.eldar_proyect.dto.UserInfo
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var cardsAdapter: CardsAdapter
    private lateinit var dataBaseHelper: DataBaseHelper
    private val cardList = mutableListOf<UserInfo>()
    private val ADD_CARD_REQUEST_CODE = 1
    private lateinit var encryptionHelper: EncryptionHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBaseHelper = DataBaseHelper(this)
        encryptionHelper = EncryptionHelper()

        val userId = intent.getIntExtra("userId", -1)

        loadUserCards(userId)

        cardsAdapter = CardsAdapter(cardList)
        binding.cardsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = cardsAdapter
        }

        binding.btnAddCard.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            intent.putExtra("userId", userId) // Pasar el userId al AddCardActivity
            startActivityForResult(intent, ADD_CARD_REQUEST_CODE)
        }
    }

    @SuppressLint("Range")
    private fun loadUserCards(userId: Int) {
        val cursor: Cursor = dataBaseHelper.getCardsByUser(userId)

        if (cursor.moveToFirst()) {
            do {
                val cardNumber = cursor.getString(cursor.getColumnIndex("cardNumber"))
                val name = cursor.getString(cursor.getColumnIndex("name")).uppercase()
                val surname = cursor.getString(cursor.getColumnIndex("surname")).uppercase()
                val cardType = cursor.getString(cursor.getColumnIndex("cardType")).uppercase()
                val decryptCardNumber = encryptionHelper.decrypt(cardNumber)

                cardList.add(UserInfo(userId, "", "", name, surname, decryptCardNumber, cardType))
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_CARD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val userInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                data?.getParcelableExtra("userInfo", UserInfo::class.java)
            } else {
                data?.getParcelableExtra<UserInfo>("userInfo")
            }

            userInfo?.let {
                cardList.add(it)
                cardsAdapter.notifyItemInserted(cardList.size - 1)
            }
        }
    }
}
