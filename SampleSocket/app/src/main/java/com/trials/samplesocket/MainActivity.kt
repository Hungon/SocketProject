package com.trials.samplesocket

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.trials.samplesocket.network.ClientSocket
import com.trials.samplesocket.network.MyServerSocket
import kotlinx.android.synthetic.main.activity_main.*
import android.arch.lifecycle.ViewModelProviders
import android.util.Log
import com.trials.samplesocket.network.SearchRemoteDevices
import com.trials.samplesocket.room.entity.DeviceEntity
import com.trials.samplesocket.viewmodel.DeviceViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var clientSocket: ClientSocket
    private lateinit var serverSocket: MyServerSocket
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var searchRemoteDevices: SearchRemoteDevices
    private val requestPermissions = listOf(Manifest.permission.ACCESS_NETWORK_STATE)
    private var requestCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set adapter
        val viewManager = LinearLayoutManager(this)
        val deviceAdapter = DevicesAdapter(this)
        list_devices.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // specify an viewAdapter (see also next example)
            adapter = deviceAdapter
            // use a linear layout manager
            layoutManager = viewManager
        }

        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
        deviceViewModel.allDevices.observe(this,
            Observer<List<DeviceEntity>> { t ->
                deviceAdapter.setDevices(t)
            })

    }

    override fun onResume() {
        super.onResume()
        val port = 7070
        if (!::serverSocket.isInitialized) {
            serverSocket = MyServerSocket(port)
        }
        serverSocket.start()
        if (!::searchRemoteDevices.isInitialized) {
            searchRemoteDevices = SearchRemoteDevices(application, port)
        }
        searchRemoteDevices.setSearchInterface(object : SearchRemoteDevices.SearchDevicesInterface() {
            override fun onComplete(devices: List<String>) {
                Log.d(TAG, "onComplete() devices -> $devices")
            }
            override fun onDetected(ip: String) {
                Log.d(TAG, "onComplete() devices -> $ip")
                val device = DeviceEntity()
                device.deviceName = ip
                deviceViewModel.insert(device)
            }
            override fun onNothing() {
                Log.d(TAG, "onNothing()")
            }
        })
        searchRemoteDevices.searchDevices()
    }

    override fun onPause() {
        super.onPause()
        if (::serverSocket.isInitialized) {
            serverSocket.close()
        }
        if (::clientSocket.isInitialized) {
            clientSocket.close()
        }
        if (::searchRemoteDevices.isInitialized) {
            searchRemoteDevices.cancelSearch()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 100) {
            if (!requestRequiredPermissions()) {

            }
        }
    }

    private fun requestRequiredPermissions(): Boolean {
        if (requestCount < 3) {
            val requiredPermissions = ArrayList<String>()
            for (p in requestPermissions) {
                if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                    requiredPermissions.add(p)
                }
            }
            if (requiredPermissions.isNotEmpty()) {
                requestCount++
                requestPermissions(requiredPermissions.toTypedArray(), 100)
                return true
            }
        }
        return false
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
