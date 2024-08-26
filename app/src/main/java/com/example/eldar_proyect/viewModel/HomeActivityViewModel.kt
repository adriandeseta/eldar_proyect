import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eldar_proyect.data.DataBaseHelper
import com.example.eldar_proyect.dto.UserInfo
import EncryptionHelper

class HomeViewModel(private val dataBaseHelper: DataBaseHelper, private val encryptionHelper: EncryptionHelper) : ViewModel() {

    private val _cardList = MutableLiveData<List<UserInfo>>()
    val cardList: LiveData<List<UserInfo>> = _cardList

    fun loadUserCards(userId: Int) {
        val cardList = mutableListOf<UserInfo>()
        val cursor = dataBaseHelper.getCardsByUser(userId)

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

        _cardList.value = cardList
    }

    fun addCard(userInfo: UserInfo) {
        val updatedCardList = _cardList.value?.toMutableList() ?: mutableListOf()
        updatedCardList.add(userInfo)
        _cardList.value = updatedCardList
    }
}
