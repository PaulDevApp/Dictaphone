package com.appsforlife.dictaphone.support

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.appsforlife.dictaphone.AppSettings
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

    fun setTheme() {
        when {
            AppSettings.getInstance()?.getThemeMode().equals(Constants.AUTO_MODE) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            AppSettings.getInstance()?.getThemeMode().equals(Constants.LIGHT_MODE) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }
}