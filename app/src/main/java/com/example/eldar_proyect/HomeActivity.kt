package com.example.eldar_proyect

import UserInfo
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eldar_proyect.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var cardsAdapter: CardsAdapter
    private val cardList = mutableListOf<UserInfo>() // Lista mutable para las tarjetas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el RecyclerView y el Adapter
        cardsAdapter = CardsAdapter(cardList)
        binding.cardsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = cardsAdapter
        }

        // Botón para agregar una nueva tarjeta
        binding.btnAddCard.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            startActivity(intent)
        }

        // Verificar si el Intent contiene los datos del UserInfo
        val userInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("userInfo", UserInfo::class.java)
        } else {
            intent.getParcelableExtra<UserInfo>("userInfo")
        }

        // Si los datos están presentes, actualiza la lista del RecyclerView
        userInfo?.let {
            cardList.add(it) // Añadir el nuevo objeto a la lista
            cardsAdapter.notifyDataSetChanged() // Notificar al Adapter que los datos han cambiado
        }
    }
}
