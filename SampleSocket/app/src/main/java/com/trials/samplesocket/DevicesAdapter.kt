package com.trials.samplesocket

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.trials.samplesocket.room.entity.DeviceEntity
import java.util.*

class DevicesAdapter(context: Context) : RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var devices: List<DeviceEntity>? = null

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeviceViewHolder {
        // create a new view
        val view = inflater.inflate(R.layout.list_devices, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return DeviceViewHolder(view, this)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        devices?.run {
            holder.deviceName.text = get(position).deviceName
        }
    }

    override fun getItemCount() = devices?.size ?: 0

    fun setDevices(_devices: List<DeviceEntity>?) {
        if (_devices != null) {
            devices = _devices
            notifyDataSetChanged()
        }
    }

    class DeviceViewHolder : RecyclerView.ViewHolder {
        var deviceName: TextView
        val devicesAdapter: DevicesAdapter

        constructor(view: View, adapter: DevicesAdapter) : super(view) {
            deviceName = view.findViewById(R.id.device)
            devicesAdapter = adapter
        }
    }
}