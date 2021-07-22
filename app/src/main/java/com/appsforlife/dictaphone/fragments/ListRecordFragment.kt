package com.appsforlife.dictaphone.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.dictaphone.R
import com.appsforlife.dictaphone.adapters.RecordAdapter
import com.appsforlife.dictaphone.database.RecordDB
import com.appsforlife.dictaphone.databinding.FragmentListRecordBinding
import com.appsforlife.dictaphone.dialogFragments.RemoveFragmentDialog
import com.appsforlife.dictaphone.listeners.PopupMenuClickListener
import com.appsforlife.dictaphone.listeners.RecordItemClickListener
import com.appsforlife.dictaphone.model.Record
import com.appsforlife.dictaphone.support.Constants.MENU_DELETE
import com.appsforlife.dictaphone.support.Constants.MENU_SHARE
import com.appsforlife.dictaphone.viewModelFactory.ListViewModelFactory
import com.appsforlife.dictaphone.viewModels.ListViewModel
import java.io.File


class ListRecordFragment : Fragment(), RecordItemClickListener, PopupMenuClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentListRecordBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list_record, container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = RecordDB.getInstance(application).recordDAO
        val viewModelFactory = ListViewModelFactory(dataSource)

        val listRecordViewModel =
            ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

        binding.listRecordViewModel = listRecordViewModel

        val adapter = RecordAdapter(this, this)
        binding.rvList.setHasFixedSize(true)
        binding.rvList.adapter = adapter


        listRecordViewModel.records.observe(viewLifecycleOwner, {
            it?.let {
                if (it.size == 0) {
                    binding.lottieEmpty.visibility = View.VISIBLE
                } else {
                    binding.lottieEmpty.visibility = View.GONE
                    adapter.data = it
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
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context

        popupMenu.menu.add(0, MENU_SHARE, Menu.NONE, context.getString(R.string.share))
        popupMenu.menu.add(0, MENU_DELETE, Menu.NONE, context.getString(R.string.delete))

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                MENU_SHARE -> {
                    shareAudio(record)
                }
                MENU_DELETE -> {
                    removeRecordDialog(record)
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    private fun removeRecordDialog(record: Record) {
        val removeFragmentDialog: RemoveFragmentDialog =
            RemoveFragmentDialog()
                .newInstance(
                    record.id,
                    record.filePath
                )
        val transaction: FragmentTransaction =
            (context as FragmentActivity)
                .supportFragmentManager
                .beginTransaction()
        removeFragmentDialog.show(transaction, "dialog_remove")
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
}