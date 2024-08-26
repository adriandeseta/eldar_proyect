package com.example.eldar_proyect

import EncryptionHelper
import com.example.eldar_proyect.viewModel.HomeViewModel
import HomeViewModelFactory
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eldar_proyect.data.DataBaseHelper
import com.example.eldar_proyect.databinding.ActivityHomeBinding
import com.example.eldar_proyect.dto.UserInfo

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(DataBaseHelper(this), EncryptionHelper())
    }
    private lateinit var cardsAdapter: CardsAdapter
    private val ADD_CARD_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getIntExtra("userId", -1)

        cardsAdapter = CardsAdapter(mutableListOf())
        binding.cardsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = cardsAdapter
        }

        // Observar los cambios en la lista de tarjetas
        homeViewModel.cardList.observe(this, Observer { cardList ->
            cardsAdapter.updateCards(cardList)
        })

        homeViewModel.loadUserCards(userId)

        binding.btnAddCard.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            intent.putExtra("userId", userId)
            startActivityForResult(intent, ADD_CARD_REQUEST_CODE)
        }
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
                homeViewModel.addCard(it)
            }
        }
    }
}
