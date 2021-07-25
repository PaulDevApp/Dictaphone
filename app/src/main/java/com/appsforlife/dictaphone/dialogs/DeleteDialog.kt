package com.appsforlife.dictaphone.dialogs

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.appsforlife.dictaphone.R
import com.appsforlife.dictaphone.listeners.DialogDeleteListener
import com.appsforlife.dictaphone.model.Record

class DeleteDialog(
    private val activity: Activity,
    private val dialogDeleteListener: DialogDeleteListener
) {

    fun createDeleteRecordDialog(record: Record) {
        val builder = AlertDialog.Builder(
            activity
        )
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.layout_dialog_delet_record, null)
        builder.setView(view)
        val dialogDelete = builder.create()
        if (dialogDelete.window != null) {
            dialogDelete.window!!.setBackgroundDrawable(ColorDrawable())
        }
        view.findViewById<View>(R.id.tv_yes).setOnClickListener {
            dialogDeleteListener.onDeleteClickListener(record)
            dialogDelete.cancel()
        }

        dialogDelete.show()
    }

}