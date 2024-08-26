import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eldar_proyect.data.DataBaseHelper
import EncryptionHelper

class HomeViewModelFactory(
    private val dataBaseHelper: DataBaseHelper,
    private val encryptionHelper: EncryptionHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dataBaseHelper, encryptionHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
