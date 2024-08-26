package com.example.eldar_proyect.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eldar_proyect.data.DataBaseHelper
import EncryptionHelper
import com.example.eldar_proyect.viewModel.AddCardViewModel

class AddCardViewModelFactory(
    private val dataBaseHelper: DataBaseHelper,
    private val encryptionHelper: EncryptionHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddCardViewModel(dataBaseHelper, encryptionHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
