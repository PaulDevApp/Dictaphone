package com.appsforlife.dictaphone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.appsforlife.dictaphone.R
import com.appsforlife.dictaphone.databinding.ItemRecordBinding
import com.appsforlife.dictaphone.dialogFragments.RemoveFragmentDialog
import com.appsforlife.dictaphone.fragments.PlayerFragment
import com.appsforlife.dictaphone.model.Record
import com.appsforlife.dictaphone.support.Utilities
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit


class RecordAdapter : RecyclerView.Adapter<RecordAdapter.ViewHolder>() {

    private var lastPosition = -1

    var data = listOf<Record>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecordBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val record: Record = data[position]
        val itemDuration: Long = record.length
        val minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(itemDuration) - TimeUnit.MINUTES.toSeconds(minutes)

        with(holder.binding) {
            tvFileName.text = record.name
            tvFileLength.text = String.format("%02d:%02d", minutes, seconds)
            tvDate.text = record.date

            val cardColors: IntArray =
                context.resources.getIntArray(R.array.card_colors)
            val backgroundColor = cardColors[Random().nextInt(cardColors.size)]
            clLayout.setBackgroundColor(backgroundColor)

            clLayout.setOnClickListener {
                val filePath = record.filePath
                val file = File(filePath)
                if (file.exists()) {
                    try {
                        playRecord(filePath, context)
                    } catch (e: Exception) {

                    }
                } else {
                    Utilities.getToast(context, R.string.file_not_found)
                }
            }

            clLayout.setOnLongClickListener {
                removeRecordDialog(record, context)
                false
            }

            if (position > lastPosition) {
                cardView.animation = AnimationUtils.loadAnimation(context, R.anim.fall_down_item)
                lastPosition = position
            }

        }
    }

    class ViewHolder(internal val binding: ItemRecordBinding) :
        RecyclerView.ViewHolder(binding.root)

    private fun playRecord(filePath: String, context: Context?) {
        val playerFragment: PlayerFragment = PlayerFragment().newInstance(filePath)
        val transaction: FragmentTransaction = (context as FragmentActivity)
            .supportFragmentManager
            .beginTransaction()
        playerFragment.show(transaction, "dialog_playback")
    }

    private fun removeRecordDialog(
        record: Record,
        context: Context?
    ) {
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
}
