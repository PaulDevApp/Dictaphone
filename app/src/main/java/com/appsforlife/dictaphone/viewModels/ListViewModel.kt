package com.appsforlife.dictaphone.viewModels

import androidx.lifecycle.ViewModel
import com.appsforlife.dictaphone.database.RecordDAO

class ListViewModel(dataSource: RecordDAO) : ViewModel() {
    val dataBase = dataSource
    val records = dataBase.getAllRecords()
}