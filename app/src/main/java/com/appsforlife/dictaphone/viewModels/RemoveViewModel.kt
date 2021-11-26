package com.appsforlife.dictaphone.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.appsforlife.dictaphone.R
import com.appsforlife.dictaphone.database.RecordDAO
import com.appsforlife.dictaphone.database.RecordDB
import com.appsforlife.dictaphone.support.Utilities
import kotlinx.coroutines.*
import java.io.File

class RemoveViewModel(
    private var recordDAO: RecordDAO,
    private val application: Application
) : ViewModel() {

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun removeRecord(recordId: Int) {
        recordDAO = RecordDB.getInstance(application).recordDAO

        try {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    recordDAO.removeRecord(recordId)
                }
            }
        } catch (e: Exception) {
            Log.e("removeRecord", "exception", e)
        }
    }

    fun removeFile(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
            Utilities.getToast(application, R.string.file_deleted_text)
        }
    }


}