package com.appsforlife.dictaphone.support

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

object Utilities {

    fun getToast(context: Context, id: Int) {
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show()
    }

    fun getDate(): String? {
        return SimpleDateFormat(
            "dd.MM.yyyy HH:mm",
            Locale.getDefault()
        ).format(Date())
    }
}