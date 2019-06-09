package com.trials.samplesocket.room.repository

import android.app.Application
import android.os.AsyncTask
import android.arch.lifecycle.LiveData
import android.content.Context
import com.trials.samplesocket.room.dao.DeviceDao
import com.trials.samplesocket.room.db.DeviceDatabase
import com.trials.samplesocket.room.entity.DeviceEntity


class DeviceRepository(application: Context) {

    private val deviceDao: DeviceDao
    val allDevices: LiveData<List<DeviceEntity>>

    init {
        val db = DeviceDatabase.getDatabase(application)
        deviceDao = db.deviceDao()
        allDevices = deviceDao.getAllDevices()
    }

    fun insert(word: DeviceEntity) {
        InsertAsyncTask(deviceDao).execute(word)
    }

    private class InsertAsyncTask internal constructor(private val asyncTaskDao: DeviceDao) :
        AsyncTask<DeviceEntity, Void, Void>() {

        override fun doInBackground(vararg params: DeviceEntity): Void? {
            asyncTaskDao.insert(params[0])
            return null
        }
    }
}