package com.appsforlife.dictaphone.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record_table")
data class Record(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "filePath")
    var filePath: String = "",
    @ColumnInfo(name = "length")
    var length: Long = 0L,
    @ColumnInfo(name = "time")
    var time: Long = 0L,
    @ColumnInfo(name = "date")
    var date: String = "",
    @ColumnInfo(name = "fileSize")
    var fileSize: String = "",
    @ColumnInfo(name = "bitrate")
    var bitrate: String = ""
)