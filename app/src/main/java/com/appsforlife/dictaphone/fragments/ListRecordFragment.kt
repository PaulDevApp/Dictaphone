package com.appsforlife.dictaphone.fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.widget.CustomPopupMenu
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.dictaphone.R
import com.appsforlife.dictaphone.activities.MainActivity
import com.appsforlife.dictaphone.adapters.RecordAdapter
import com.appsforlife.dictaphone.database.RecordDB
import com.appsforlife.dictaphone.databinding.FragmentListRecordBinding
import com.appsforlife.dictaphone.dialogs.DeleteDialog
import com.appsforlife.dictaphone.listeners.DialogDeleteListener
import com.appsforlife.dictaphone.listeners.PopupMenuClickListener
import com.appsforlife.dictaphone.listeners.RecordItemClickListener
import com.appsforlife.dictaphone.model.Record
import com.appsforlife.dictaphone.support.Constants.MENU_DELETE
import com.appsforlife.dictaphone.support.Constants.MENU_OPEN_WITH
import com.appsforlife.dictaphone.support.Constants.MENU_SHARE
import com.appsforlife.dictaphone.support.Utilities
import com.appsforlife.dictaphone.viewModelFactory.ListViewModelFactory
import com.appsforlife.dictaphone.viewModelFactory.RemoveViewModelFactory
import com.appsforlife.dictaphone.viewModels.ListViewModel
import com.appsforlife.dictaphone.viewModels.RemoveViewModel
import java.io.File


class ListRecordFragment : Fragment(), RecordItemClickListener, PopupMenuClickListener,
    DialogDeleteListener {

    private lateinit var deleteDialog: DeleteDialog
    private lateinit var removeViewModel: RemoveViewModel
    private lateinit var mainActivity: MainActivity
    private lateinit var animation: LayoutAnimationController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding: FragmentListRecordBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list_record, container, false
        )

        val application = requireNotNull(this.activity).application

        mainActivity = activity as MainActivity

        val dataSource = RecordDB.getInstance(application).recordDAO
        val viewModelFactory = ListViewModelFactory(dataSource)

        val recordDAO = RecordDB.getInstance(application).recordDAO
        val removeViewModelFactory = RemoveViewModelFactory(recordDAO, application)
        removeViewModel =
            ViewModelProvider(this, removeViewModelFactory).get(RemoveViewModel::class.java)
        deleteDialog = DeleteDialog(this.activity as MainActivity, this)

        val listRecordViewModel =
            ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

        binding.listRecordViewModel = listRecordViewModel

        animation = AnimationUtils.loadLayoutAnimation(
            context,
            R.anim.fall_down_layout
        )

        val adapter = RecordAdapter(this, this)
        binding.rvList.setHasFixedSize(true)
        binding.rvList.adapter = adapter

        listRecordViewModel.records.observe(viewLifecycleOwner, {
            it?.let {
                if (it.size == 0) {
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.rvList.visibility = View.GONE
                } else {
                    binding.lottieEmpty.visibility = View.GONE
                    binding.rvList.visibility = View.VISIBLE
                    adapter.setData(it)
                    if (flag == 1) {
                        binding.rvList
                            .layoutAnimation = animation
                        flag = 2
                    }
                }
            }
        })

        binding.lifecycleOwner = this

        return binding.root
    }

    private fun playRecord(filePath: String) {
        val playerFragment: PlayerFragment = PlayerFragment().newInstance(filePath)
        val transaction: FragmentTransaction = (context as FragmentActivity)
            .supportFragmentManager
            .beginTransaction()
        playerFragment.show(transaction, "dialog_playback")
    }

    private fun showPopupMenu(view: View, record: Record) {
        val popupMenu = CustomPopupMenu(view.context, view)
        val context = view.context

        popupMenu.menu.add(0, MENU_SHARE, Menu.NONE, context.getString(R.string.share))
            .setIcon(R.drawable.ic_share)
        popupMenu.menu.add(0, MENU_OPEN_WITH, Menu.NONE, R.string.to_open_with)
            .setIcon(R.drawable.ic_to_open_with)
        popupMenu.menu.add(0, MENU_DELETE, Menu.NONE, context.getString(R.string.delete))
            .setIcon(R.drawable.ic_delete)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                MENU_SHARE -> {
                    shareAudio(record)
                }
                MENU_DELETE -> {
                    deleteDialog.createDeleteRecordDialog(record)
                }
                MENU_OPEN_WITH -> {
                    openWith(record)
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    private fun openWith(record: Record) {
        val intent = Intent()
        try {
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(Uri.parse(record.filePath), "audio/*")
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Utilities.getToast(mainActivity, R.string.no_suitable_applications_to_open_the_file)
        }
    }


    private fun shareAudio(record: Record) {
        val uri = FileProvider.getUriForFile(
            context as Activity,
            "com.appsforlife.dictaphone.android.fileprovider",
            File(record.filePath)
        )
        val shareIntent: Intent = ShareCompat.IntentBuilder.from(context as Activity)
            .setType("audio/mp3")
            .setStream(uri)
            .intent

        startActivity(Intent.createChooser(shareIntent, "Share record file"))
    }

    override fun onItemClick(filePath: String) {
        playRecord(filePath)
    }

    override fun onMenuClick(view: View, record: Record) {
        showPopupMenu(view, record)
    }

    override fun onDeleteClickListener(record: Record) {
        try {
            record.id.let { removeViewModel.removeRecord(it) }
            record.filePath.let { removeViewModel.removeFile(it) }
        } catch (e: java.lang.Exception) {
            Log.e("deleteFileDialog", "exception", e)
        }
    }

    companion object {
        private var flag = 1
    }

}