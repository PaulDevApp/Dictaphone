package com.appsforlife.dictaphone.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.appsforlife.dictaphone.AppSettings
import com.appsforlife.dictaphone.R
import com.appsforlife.dictaphone.activities.MainActivity
import com.appsforlife.dictaphone.database.RecordDAO
import com.appsforlife.dictaphone.database.RecordDB
import com.appsforlife.dictaphone.model.Record
import com.appsforlife.dictaphone.support.Constants
import com.appsforlife.dictaphone.support.Utilities
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat

class RecordService : Service() {

    private var fileName: String? = null
    private var filePath: String? = null

    private var mediaRecorder: MediaRecorder? = null

    private var startingTimeMillis: Long = 0
    private var elapsedMillis: Long = 0

    private var recordDAO: RecordDAO? = null

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        recordDAO = RecordDB.getInstance(application).recordDAO
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startRecording()
        return START_NOT_STICKY
    }

    private fun startRecording() {
        setFileNameAndPath()

        mediaRecorder = MediaRecorder()

        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setOutputFile(filePath)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setAudioChannels(1)
        AppSettings.getInstance()?.getBitrate()?.let { mediaRecorder?.setAudioEncodingBitRate(it) }

        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            startingTimeMillis = System.currentTimeMillis()
            startForeground(1, createNotification())
        } catch (e: IOException) {
            Log.e("RecordService", "prepare failed")
        }
    }

    private fun createNotification(): Notification {
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            Constants.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_record_voice)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_recording))
            .setOngoing(true)
        mBuilder.setContentIntent(
            PendingIntent.getActivities(
                applicationContext, 0, arrayOf(
                    Intent(
                        applicationContext,
                        MainActivity::class.java
                    )
                ), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
        return mBuilder.build()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setFileNameAndPath() {
        var count = 0
        var f: File
        val dateTime = SimpleDateFormat("mm_dd_hh_mm_ss").format(System.currentTimeMillis())

        do {
            fileName = (getString(R.string.default_file_name)
                    + "_" + dateTime + count + AppSettings.getInstance()?.getFormat())
            filePath = application.getExternalFilesDir(null)?.absolutePath
            filePath += "/$fileName"

            count++

            f = File(filePath!!)
        } while (f.exists() && !f.isDirectory)
    }

    private fun stopRecording() {
        val record = Record()

        mediaRecorder?.stop()
        elapsedMillis = System.currentTimeMillis() - startingTimeMillis
        mediaRecorder?.release()
        Utilities.getToast(this, R.string.toast_recording_finish)

        record.name = fileName.toString()
        record.filePath = filePath.toString()
        record.fileSize = getSize(filePath.toString())
        record.length = elapsedMillis
        record.time = System.currentTimeMillis()
        record.date = Utilities.getDate().toString()
        record.bitrate = AppSettings.getInstance()?.getBitrate().toString().dropLast(3) + " kbps"


        mediaRecorder = null

        try {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    recordDAO?.insert(record)
                }
            }
        } catch (e: Exception) {
            Log.e("RecordService", "exception", e)
        }
    }

    override fun onDestroy() {
        if (mediaRecorder != null) {
            stopRecording()
        }

        super.onDestroy()
    }

    @SuppressLint("DefaultLocale")
    private fun getSize(filePath: String): String {
        val file = File(filePath)
        val fileSizeInKB = file.length().toDouble() / 1024
        return java.lang.String.format("%.2f" + " KB", fileSizeInKB)
    }

}