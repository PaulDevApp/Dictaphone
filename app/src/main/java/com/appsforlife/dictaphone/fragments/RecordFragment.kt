package com.appsforlife.dictaphone.fragments

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.dictaphone.MainActivity
import com.appsforlife.dictaphone.R
import com.appsforlife.dictaphone.database.RecordDAO
import com.appsforlife.dictaphone.database.RecordDB
import com.appsforlife.dictaphone.databinding.FragmentRecordBinding
import com.appsforlife.dictaphone.service.RecordService
import com.appsforlife.dictaphone.support.Constants
import com.appsforlife.dictaphone.support.Utilities
import com.appsforlife.dictaphone.viewModels.RecordViewModel
import java.io.File


class RecordFragment : Fragment() {

    private lateinit var viewModel: RecordViewModel
    private lateinit var mainActivity: MainActivity
    private var recordDAO: RecordDAO? = null
    private lateinit var binding: FragmentRecordBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                if (mainActivity.isServiceRunning()) {
                    onRecord(false)
                    viewModel.stopTimer()
                } else {
                    onRecord(true)
                    viewModel.startTimer()
                }
            } else {
                Utilities.getToast(mainActivity, R.string.toast_recording_permissions)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_record, container, false
        )

        recordDAO = context?.let { RecordDB.getInstance(it).recordDAO }

        mainActivity = activity as MainActivity

        viewModel = ViewModelProvider(this).get(RecordViewModel::class.java)

        binding.recordViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        if (!mainActivity.isServiceRunning()) {
            viewModel.resetTimer()
            binding.lottieRecording.cancelAnimation()
        } else {
            binding.lottieRecording.playAnimation()
        }

        binding.lottieRecording.setOnClickListener { requestPermission() }

        createChannel()

        return binding.root
    }

    private fun onRecord(start: Boolean) {
        val intent = Intent(activity, RecordService::class.java)

        if (start) {
            binding.lottieRecording.playAnimation()
            activity?.let { Utilities.getToast(it, R.string.toast_recording_start) }

            val folder =
                File(activity?.getExternalFilesDir(null)?.absolutePath.toString())
            if (!folder.exists()) {
                folder.mkdir()
            }

            activity?.startService(intent)
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            binding.lottieRecording.cancelAnimation()

            activity?.stopService(intent)
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Constants.CHANNEL_ID,
                Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
                .apply {
                    setShowBadge(false)
                    setSound(null, null)
                }
            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mainActivity.isServiceRunning()) {
            binding.lottieRecording.cancelAnimation()
        } else {
            binding.lottieRecording.playAnimation()
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
}