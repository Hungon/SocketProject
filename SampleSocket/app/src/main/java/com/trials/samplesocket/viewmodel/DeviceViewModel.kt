package com.trials.samplesocket.viewmodel

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.AndroidViewModel
import com.trials.samplesocket.room.entity.DeviceEntity
import com.trials.samplesocket.room.repository.DeviceRepository


class DeviceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DeviceRepository(application)

    val allDevices: LiveData<List<DeviceEntity>>

    init {
        allDevices = repository.allDevices
    }

    fun insert(device: DeviceEntity) {
        repository.insert(device)
    }
}
