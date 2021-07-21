package com.appsforlife.dictaphone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appsforlife.dictaphone.model.Record
import com.appsforlife.dictaphone.support.Constants

@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class RecordDB : RoomDatabase(){

    abstract val recordDAO: RecordDAO

    companion object {

        @Volatile
        private var INSTANCE: RecordDB? = null

        fun getInstance(context: Context):RecordDB{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecordDB::class.java,
                        Constants.NAME_DB
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}