package com.appsforlife.dictaphone.listeners

import android.view.View
import com.appsforlife.dictaphone.model.Record

interface PopupMenuClickListener {
    fun onMenuClick(view: View, record: Record)
}