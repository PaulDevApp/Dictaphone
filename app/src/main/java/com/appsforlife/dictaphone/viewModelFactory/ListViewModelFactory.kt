package com.appsforlife.dictaphone.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.dictaphone.database.RecordDAO
import com.appsforlife.dictaphone.support.Constants
import com.appsforlife.dictaphone.viewModels.ListViewModel

class ListViewModelFactory(private val databaseDao: RecordDAO) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(databaseDao) as T
        }
        throw IllegalArgumentException(Constants.UNKNOWN_VIEW_MODEL_CLASS)
    }

}