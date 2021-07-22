package com.appsforlife.dictaphone.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.dictaphone.database.RecordDAO
import com.appsforlife.dictaphone.viewModels.ListViewModel
import java.lang.IllegalArgumentException

class ListViewModelFactory(private val databaseDao: RecordDAO) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(databaseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}