package com.appsforlife.dictaphone.listeners

import com.appsforlife.dictaphone.model.Record

interface DialogDeleteListener {
    fun onDeleteClickListener(record: Record)
}