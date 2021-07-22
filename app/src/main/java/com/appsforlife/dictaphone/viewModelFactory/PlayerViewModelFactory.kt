package com.appsforlife.dictaphone.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.dictaphone.viewModels.PlayerViewModel

class PlayerViewModelFactory(
    private val mediaPath: String,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(mediaPath, application) as T
        }
        throw IllegalArgumentException("unknown ViewModel class")
    }
}