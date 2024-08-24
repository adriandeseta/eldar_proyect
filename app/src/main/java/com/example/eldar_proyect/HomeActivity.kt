package com.example.eldar_proyect

import CardsAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eldar_proyect.databinding.ActivityHomeBinding
import com.example.eldar_proyect.dto.UserInfo

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var cardsAdapter: CardsAdapter
    private val cardList = mutableListOf<UserInfo>() // Lista mutable para las tarjetas
    private val ADD_CARD_REQUEST_CODE = 1 // Código de solicitud para identificar la actividad

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
            startActivityForResult(intent, ADD_CARD_REQUEST_CODE) // Lanza AddCardActivity y espera un resultado
        }
    }

    // Método para manejar el resultado de AddCardActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_CARD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val userInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                data?.getParcelableExtra("userInfo", UserInfo::class.java)
            } else {
                data?.getParcelableExtra<UserInfo>("userInfo")
            }

            // Si se recibió un UserInfo, añadirlo a la lista y actualizar el RecyclerView
            userInfo?.let {
                cardList.add(it) // Añadir el nuevo objeto a la lista
                cardsAdapter.notifyItemInserted(cardList.size - 1) // Notificar al Adapter que se ha insertado un nuevo ítem
            }
        }
    }
}
