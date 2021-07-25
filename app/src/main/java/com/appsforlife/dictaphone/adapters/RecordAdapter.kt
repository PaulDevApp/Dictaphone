package com.appsforlife.dictaphone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import com.appsforlife.dictaphone.R
import com.appsforlife.dictaphone.databinding.ItemRecordBinding
import com.appsforlife.dictaphone.listeners.PopupMenuClickListener
import com.appsforlife.dictaphone.listeners.RecordItemClickListener
import com.appsforlife.dictaphone.model.Record
import com.appsforlife.dictaphone.support.DiffUtil
import com.appsforlife.dictaphone.support.Utilities
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit


class RecordAdapter(
    private val recordItemClickListener: RecordItemClickListener,
    private val popupMenuClickListener: PopupMenuClickListener
) : RecyclerView.Adapter<RecordAdapter.ViewHolder>() {

    private var lastPosition = -1

    private var oldRecordList = emptyList<Record>()

//    var data = listOf<Record>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    override fun getItemCount() = oldRecordList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecordBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val record: Record = oldRecordList[position]
        val itemDuration: Long = record.length
        val minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(itemDuration) - TimeUnit.MINUTES.toSeconds(minutes)

        with(holder.binding) {
            tvFileName.text = record.name
            tvLength.text = String.format("%02d:%02d", minutes, seconds)
            tvDate.text = record.date
            tvFileSize.text = record.fileSize
            tvBitrate.text = record.bitrate

            val cardColors: IntArray =
                context.resources.getIntArray(R.array.card_colors)
            val backgroundColor = cardColors[Random().nextInt(cardColors.size)]
            clLayout.setBackgroundColor(backgroundColor)

            clLayout.setOnClickListener {
                val filePath = record.filePath
                val file = File(filePath)
                if (file.exists()) {
                    try {
                        recordItemClickListener.onItemClick(filePath)
                    } catch (e: Exception) {
                        Utilities.getToast(context, R.string.file_not_found)
                    }
                } else {
                    Utilities.getToast(context, R.string.file_not_found)
                }
            }

            ivPopupMenu.setOnClickListener {
                popupMenuClickListener.onMenuClick(ivPopupMenu, record)
            }

            if (position > lastPosition) {
                cardView.animation = AnimationUtils.loadAnimation(context, R.anim.fall_down_item)
                lastPosition = position
            }

        }
    }

    fun setData(newRecordList: List<Record>) {
        val diffUtil = DiffUtil(oldRecordList, newRecordList)
        val diffUtilResult = calculateDiff(diffUtil)
        oldRecordList = newRecordList
        diffUtilResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(internal val binding: ItemRecordBinding) :
        RecyclerView.ViewHolder(binding.root)
}
