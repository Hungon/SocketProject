package com.trials.samplesocket.room.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.content.Context
import com.trials.samplesocket.room.dao.DeviceDao
import com.trials.samplesocket.room.entity.DeviceEntity
import android.os.AsyncTask
import android.arch.persistence.db.SupportSQLiteDatabase
import android.support.annotation.NonNull
import android.arch.persistence.room.RoomDatabase


@Database(entities = [DeviceEntity::class], version = 1, exportSchema = false)
abstract class DeviceDatabase : RoomDatabase() {

    abstract fun deviceDao(): DeviceDao

    companion object {
        private lateinit var INSTANCE: DeviceDatabase

        internal fun getDatabase(context: Context): DeviceDatabase {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    DeviceDatabase::class.java, "device_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomDatabaseCallback)
                    .build()
            }
            return INSTANCE
        }

        /**
         * Override the onOpen method to populate the database.
         * For this sample, we clear the database every time it is created or opened.
         */
        private val roomDatabaseCallback = object : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
//                PopulateDbAsync(INSTANCE).execute()
            }
        }

        /**
         * Populate the database in the background.
         * If you want to start with more words, just add them.
         */
        private class PopulateDbAsync internal constructor(db: DeviceDatabase) : AsyncTask<Void, Void, Void>() {

            private val deviceDao = db.deviceDao()
            private var devices = arrayOf("dolphin", "crocodile", "cobra")

            override fun doInBackground(vararg params: Void): Void? {
                // Start the app with a clean database every time.
                // Not needed if you only populate on creation.
                deviceDao.deleteAll()
                for (i in 0 until devices.size) {
                    val device = DeviceEntity()
                    device.deviceName = devices[i]
                    deviceDao.insert(device)
                }
                return null
            }
        }
    }

}