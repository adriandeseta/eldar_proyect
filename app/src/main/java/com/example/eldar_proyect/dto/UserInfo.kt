package com.example.eldar_proyect.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val id: Int, // ID del usuario
    val user: String, // Nombre de usuario
    val password: String, // Contraseña del usuario
    val name: String, // Nombre de la tarjeta
    val surname: String, // Apellido de la tarjeta
    val cardNumber: String, // Número de tarjeta
    val cardType: String // Tipo de tarjeta
) : Parcelable {
    // La propiedad cardType ya tiene un getter generado automáticamente,
    // por lo que no es necesario definir un método getCardType.
}
