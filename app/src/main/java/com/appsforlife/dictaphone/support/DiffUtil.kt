package com.appsforlife.dictaphone.support

import androidx.recyclerview.widget.DiffUtil
import com.appsforlife.dictaphone.model.Record

class DiffUtil(
    private val oldList: List<Record>,
    private val newList: List<Record>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }
            oldList[oldItemPosition].name != newList[newItemPosition].name -> {
                false
            }
            oldList[oldItemPosition].bitrate != newList[newItemPosition].bitrate -> {
                false
            }
            oldList[oldItemPosition].date != newList[newItemPosition].date -> {
                false
            }
            oldList[oldItemPosition].filePath != newList[newItemPosition].filePath -> {
                false
            }
            oldList[oldItemPosition].fileSize != newList[newItemPosition].fileSize -> {
                false
            }
            oldList[oldItemPosition].length != newList[newItemPosition].length -> {
                false
            }
            oldList[oldItemPosition].time != newList[newItemPosition].time -> {
                false
            }
            else -> true
        }
    }
}