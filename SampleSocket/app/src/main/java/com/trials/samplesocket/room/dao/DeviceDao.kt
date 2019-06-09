package com.trials.samplesocket.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.trials.samplesocket.room.entity.DeviceEntity

@Dao
interface DeviceDao {

    @get:Query("SELECT * from device_table ORDER BY device_name ASC")
    val allWords: List<DeviceEntity>

    @Query("SELECT * from device_table ORDER BY device_name ASC")
    fun getAllDevices(): LiveData<List<DeviceEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(device: DeviceEntity)

    @Query("DELETE FROM device_table")
    fun deleteAll()
}