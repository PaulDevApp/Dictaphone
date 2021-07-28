package com.appsforlife.dictaphone.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appsforlife.dictaphone.AppSettings
import com.appsforlife.dictaphone.R
import com.appsforlife.dictaphone.databinding.ActivitySettingsBinding
import com.appsforlife.dictaphone.dialogs.OpenSourceDialog
import com.appsforlife.dictaphone.support.Constants
import com.appsforlife.dictaphone.support.Utilities

class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding
    private lateinit var openSourceDialog: OpenSourceDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Utilities.setTheme()

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolBar_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        openSourceDialog = OpenSourceDialog(this)

        when {
            AppSettings.getInstance()?.getBitrate() == Constants.BITRATE_192 -> {
                binding.rb192.isChecked = true
            }
            AppSettings.getInstance()?.getBitrate() == Constants.BITRATE_256 -> {
                binding.rb256.isChecked = true
            }
            else -> {
                binding.rb320.isChecked = true
            }
        }

        when {
            AppSettings.getInstance()?.getFormat().equals(Constants.FORMAT_MP3) -> {
                binding.rbMp3.isChecked = true
            }
            AppSettings.getInstance()?.getFormat().equals(Constants.FORMAT_MP4) -> {
                binding.rbMp4.isChecked = true
            }
            else -> {
                binding.rbAac.isChecked = true
            }
        }

        when {
            AppSettings.getInstance()?.getThemeMode().equals(Constants.AUTO_MODE) -> {
                binding.rbAuto.isChecked = true
            }
            AppSettings.getInstance()?.getThemeMode().equals(Constants.LIGHT_MODE) -> {
                binding.rbDay.isChecked = true
            }
            else -> {
                binding.rbNight.isChecked = true
            }
        }

        binding.rgBitrate.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_192 -> {
                    AppSettings.getInstance()?.setBitrate(Constants.BITRATE_192)
                }
                R.id.rb_256 -> {
                    AppSettings.getInstance()?.setBitrate(Constants.BITRATE_256)
                }
                R.id.rb_320 -> {
                    AppSettings.getInstance()?.setBitrate(Constants.BITRATE_320)
                }
            }
        }

        binding.rgFormat.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_mp3 -> {
                    AppSettings.getInstance()?.setFormat(Constants.FORMAT_MP3)
                }
                R.id.rb_mp4 -> {
                    AppSettings.getInstance()?.setFormat(Constants.FORMAT_MP4)
                }
                R.id.rb_aac -> {
                    AppSettings.getInstance()?.setFormat(Constants.FORMAT_AAC)
                }
            }
        }

        val intent = intent
        binding.rgTheme.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_auto -> {
                    AppSettings.getInstance()?.setThemeMode(Constants.AUTO_MODE)
                    finish()
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                R.id.rb_night -> {
                    AppSettings.getInstance()?.setThemeMode(Constants.NIGHT_MODE)
                    finish()
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                R.id.rb_day -> {
                    AppSettings.getInstance()?.setThemeMode(Constants.LIGHT_MODE)
                    finish()
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            }
        }


        binding.tvRateApp.setOnClickListener {
            val appPackageName = application.packageName
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }

        binding.tvApps.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=%D0%9F%D0%B0%D0%B2%D0%B5%D0%BB+%D0%97%D1%83%D0%B1%D0%BA%D0%BE&hl=ru&gl=US")
                )
            )
        }

        binding.tvPrivacy.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://docs.google.com/document/d/1sWVTo9QvTqKO4pCqfAOSGjIQpiJWjXqEichwxSUDJss/edit?usp=sharing")
                )
            )
        }

        binding.tvOpenSource.setOnClickListener {
            openSourceDialog.createOpenSourceDialog()
        }

    }
}