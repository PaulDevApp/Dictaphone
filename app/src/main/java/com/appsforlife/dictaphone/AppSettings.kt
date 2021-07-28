package com.appsforlife.dictaphone

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.appsforlife.dictaphone.support.Constants

class AppSettings : Application() {

    private lateinit var preferences: SharedPreferences
    private var bitrate: Int? = null
    private var format: String? = null
    private var theme: String? = null

    override fun onCreate() {
        super.onCreate()

        preferences = getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
        this.bitrate = preferences.getInt(Constants.BITRATE, Constants.BITRATE_256)
        this.format = preferences.getString(Constants.FORMAT, Constants.FORMAT_MP3)
        this.theme = preferences.getString(Constants.THEME, Constants.AUTO_MODE)


        instance = this

    }

    fun setThemeMode(theme: String) {
        this.theme = theme
        val editor = preferences.edit()
        editor.apply {
            putString(Constants.THEME, theme)
        }.apply()
    }

    fun setFormat(format: String) {
        this.format = format
        val editor = preferences.edit()
        editor.apply {
            putString(Constants.FORMAT, format)
        }.apply()

    }

    fun setBitrate(bitrate: Int) {
        this.bitrate = bitrate
        val editor = preferences.edit()
        editor.apply {
            putInt(Constants.BITRATE, bitrate)
        }.apply()
    }

    fun getThemeMode(): String? {
        return theme
    }

    fun getBitrate(): Int? {
        return bitrate
    }

    fun getFormat(): String? {
        return format
    }

    companion object {
        private var instance: AppSettings? = null

        fun getInstance(): AppSettings? {
            if (instance == null) {
                instance = AppSettings()
            }
            return instance
        }
    }
}