package com.example.eldar_proyect.viewModel

import EncryptionHelper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eldar_proyect.data.DataBaseHelper
import com.example.eldar_proyect.dto.UserInfo

class AddCardViewModel(
    private val dataBaseHelper: DataBaseHelper,
    private val encryptionHelper: EncryptionHelper
) : ViewModel() {

    private val _userInfo = MutableLiveData<UserInfo?>()
    val userInfo: LiveData<UserInfo?> = _userInfo

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun formatCardNumber(input: String): String {
        return input.replace(" ", "").chunked(4).joinToString(" ")
    }

    fun addNewCard(userId: Int, userName: String, userSurname: String, userCardNumber: String) {
        if (userName.isNotEmpty() && userSurname.isNotEmpty() && userCardNumber.isNotEmpty() && userId != -1) {
            val cardType = when {
                userCardNumber.startsWith("3") -> "AMEX"
                userCardNumber.startsWith("4") -> "VISA"
                userCardNumber.startsWith("5") -> "MASTERCARD"
                else -> "Unknown"
            }

            val encryptedCardNumber = encryptionHelper.encrypt(userCardNumber)

            dataBaseHelper.addCard(userId, userName, userSurname, encryptedCardNumber, cardType)

            val newUserInfo = UserInfo(
                id = userId,
                user = "",
                password = "",
                name = userName,
                surname = userSurname,
                cardNumber = userCardNumber,
                cardType = cardType
            )

            _userInfo.value = newUserInfo
        } else {
            _errorMessage.value = "Debe completar todos los campos y seleccionar un usuario válido"
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
