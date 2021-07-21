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
import com.appsforlife.dictaphone.MainActivity
import com.appsforlife.dictaphone.R
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
        mediaRecorder?.setAudioEncodingBitRate(192000)

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
                ), 0
            )
        )
        return mBuilder.build()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setFileNameAndPath() {
        var count = 0
        var f: File
        val dateTime = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(System.currentTimeMillis())

        do {
            fileName = (getString(R.string.default_file_name)
                    + "_" + dateTime + count + ".mp4")
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
        record.length = elapsedMillis
        record.time = System.currentTimeMillis()
        record.date = Utilities.getDate().toString()


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

}