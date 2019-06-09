package com.trials.samplesocket.room.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "device_table")
class DeviceEntity {
    @PrimaryKey
    @ColumnInfo(name = "device_name")
    var deviceName = ""
    var ip = ""
}