package com.trials.samplesocket.network

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.util.Log
import kotlinx.coroutines.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

class SearchRemoteDevices(context: Application, private val fixedPort: Int) {

    private val TAG by lazy { SearchRemoteDevices::class.java.simpleName }
    private var searchJob: Job? = null
    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private var searchInterface: SearchDevicesInterface? = null

    abstract class SearchDevicesInterface {
        open fun onComplete(devices: List<String>) {}
        open fun onDetected(ip: String) {}
        open fun onNothing() {}
    }

    fun setSearchInterface(searchDevicesInterface: SearchDevicesInterface) {
        searchInterface = searchDevicesInterface
    }

    fun searchDevices() {
        cancelSearch()
        val a = getHostAddress()
        val prefixAddress = a.substring(0, a.lastIndexOf(".") + 1)
        var accessAddress: String
        var socket: Socket? = null
        var endPoint: SocketAddress
        var lastIp = 0
        val arrayOfAddresses = ArrayList<String>()
        Log.d(TAG, "searchDevices() prefix -> $prefixAddress")
        searchJob = GlobalScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    accessAddress = prefixAddress + lastIp.toString()
                    endPoint = InetSocketAddress(accessAddress, fixedPort)
                    socket = Socket()
                    socket?.connect(endPoint, 50)
                    Log.d(TAG, "searchDevices() detected remote device -> ${socket?.remoteSocketAddress}")
                    arrayOfAddresses.add(accessAddress)
                    searchInterface?.onDetected(accessAddress)
                } catch (e: IOException) {
                    Log.d(TAG, e.message)
                } finally {
                    socket?.close()
                }
                lastIp++
                if (lastIp > 254) break
            }
            Log.d(TAG, "searchDevices() detected remote devices -> $arrayOfAddresses")
            if (arrayOfAddresses.isEmpty()) {
                searchInterface?.onNothing()
            } else {
                searchInterface?.onComplete(arrayOfAddresses)
            }
        }
    }

    fun cancelSearch() {
        searchJob?.run {
            if (isActive) cancel()
        }
    }

    private fun getHostAddress(): String {
        return try {
            val wifiInfo = wifiManager.connectionInfo
            val ip = wifiInfo.ipAddress
            val hostAddress = Formatter.formatIpAddress(ip)
            Log.d(TAG, "getIpAddresses() host address as IPv4 -> $hostAddress")
            hostAddress
        } catch (e: Exception) {
            Log.e(TAG, "getIpAddresses() error -> ${e.message}")
            ""
        }
    }

}