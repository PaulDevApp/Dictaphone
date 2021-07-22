package com.appsforlife.dictaphone.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.dictaphone.database.RecordDAO
import com.appsforlife.dictaphone.support.Constants
import com.appsforlife.dictaphone.viewModels.RemoveViewModel

class RemoveViewModelFactory(
    private val recordDAO: RecordDAO,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoveViewModel::class.java)) {
            return RemoveViewModel(
                recordDAO,
                application
            ) as T
        }
        throw IllegalArgumentException(Constants.UNKNOWN_VIEW_MODEL_CLASS)
    }

}