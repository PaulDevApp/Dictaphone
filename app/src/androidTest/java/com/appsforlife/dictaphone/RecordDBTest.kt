package com.appsforlife.dictaphone

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.appsforlife.dictaphone.database.RecordDAO
import com.appsforlife.dictaphone.database.RecordDB
import com.appsforlife.dictaphone.model.Record
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class RecordDBTest {

    private lateinit var recordDao: RecordDAO
    private lateinit var recordDB: RecordDB

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        recordDB = Room.inMemoryDatabaseBuilder(context, RecordDB::class.java)
            .allowMainThreadQueries()
            .build()

        recordDao = recordDB.recordDAO
    }

    @After
    @Throws(IOException::class)
    fun closDB() {
        recordDB.close()
    }

    @Test
    @Throws(Exception::class)
    fun testDatabase() {
        recordDao.insert(Record())
        val getCount = recordDao.getCount()
        assertEquals(getCount, 1)
    }

}











