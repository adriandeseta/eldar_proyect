package com.example.eldar_proyect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eldar_proyect.dto.UserInfo

class CardsAdapter(private val cardList: MutableList<UserInfo>) : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userCardNumber: TextView = itemView.findViewById(R.id.card_number)
        val userName: TextView = itemView.findViewById(R.id.card_name)
        val userSurname: TextView = itemView.findViewById(R.id.card_surname)
        val userCardType: TextView = itemView.findViewById(R.id.card_type)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val userInfo = cardList[position]
        holder.userCardNumber.text = userInfo.cardNumber
        holder.userName.text = userInfo.name
        holder.userSurname.text = userInfo.surname
        holder.userCardType.text = userInfo.cardType

    }

    override fun getItemCount(): Int {
        return cardList.size
    }
}
