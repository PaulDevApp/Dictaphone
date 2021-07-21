package com.appsforlife.dictaphone.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.appsforlife.dictaphone.model.Record

@Dao
interface RecordDAO {

    @Insert
    fun insert(record: Record)

    @Update
    fun update(record: Record)

    @Query("SELECT * from record_table WHERE id = :key")
    fun getRecord(key: Long?): Record?

    @Query("DELETE from record_table")
    fun removeAll()

    @Query("DELETE from record_table WHERE id = :key")
    fun removeRecord(key: Long?)

    @Query("SELECT * from record_table ORDER BY id DESC")
    fun getAllRecords(): LiveData<MutableList<Record>>

    @Query("SELECT COUNT(*) from record_table")
    fun getCount(): LiveData<Int>
}