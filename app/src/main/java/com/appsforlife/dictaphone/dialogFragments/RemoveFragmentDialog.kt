package com.appsforlife.dictaphone.dialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.dictaphone.R
import com.appsforlife.dictaphone.database.RecordDB
import com.appsforlife.dictaphone.support.Constants
import com.appsforlife.dictaphone.viewModelFactory.RemoveViewModelFactory
import com.appsforlife.dictaphone.viewModels.RemoveViewModel

class RemoveFragmentDialog : DialogFragment() {

    private lateinit var removeViewModel: RemoveViewModel

    fun newInstance(recordId: Long, recordPath: String?): RemoveFragmentDialog {
        val removeFragmentDialog = RemoveFragmentDialog()
        val bundle = Bundle()
        bundle.putLong(Constants.ARG_RECORD_ID, recordId)
        bundle.putString(Constants.ARG_RECORD_PATH, recordPath)

        removeFragmentDialog.arguments = bundle

        return removeFragmentDialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val application = requireNotNull(this.activity).application
        val recordDAO = RecordDB.getInstance(application).recordDAO
        val removeViewModelFactory = RemoveViewModelFactory(recordDAO, application)
        removeViewModel =
            ViewModelProvider(this, removeViewModelFactory).get(RemoveViewModel::class.java)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val itemPath = arguments?.getString(Constants.ARG_RECORD_PATH)
        val itemId = arguments?.getLong(Constants.ARG_RECORD_ID)

        return AlertDialog.Builder(activity)
            .setTitle(R.string.dialog_title_delete)
            .setMessage(R.string.dialog_text_delete)
            .setPositiveButton(R.string.dialog_action_yes) { dialog, which ->
                try {
                    itemId?.let { removeViewModel.removeRecord(it) }
                    itemPath?.let { removeViewModel.removeFile(it) }
                } catch (e: java.lang.Exception) {
                    Log.e("deleteFileDialog", "exception", e)
                }
                dialog.cancel()
            }
            .setNegativeButton(
                R.string.dialog_action_no
            ) { dialog, _ ->
                dialog.cancel()
            }
            .create()
    }

}