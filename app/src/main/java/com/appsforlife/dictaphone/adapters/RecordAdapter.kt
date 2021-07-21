package com.appsforlife.dictaphone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appsforlife.dictaphone.databinding.ItemRecordBinding
import com.appsforlife.dictaphone.model.Record
import java.util.*
import java.util.concurrent.TimeUnit


class RecordAdapter : RecyclerView.Adapter<RecordAdapter.ViewHolder>() {

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
                context.resources.getIntArray(com.appsforlife.dictaphone.R.array.card_colors)
            val backgroundColor = cardColors[Random().nextInt(cardColors.size)]
            clLayout.setBackgroundColor(backgroundColor)

        }
    }

    class ViewHolder(internal val binding: ItemRecordBinding) :
        RecyclerView.ViewHolder(binding.root)
}