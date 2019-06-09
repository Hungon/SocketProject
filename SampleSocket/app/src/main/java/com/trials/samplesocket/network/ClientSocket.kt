package com.trials.samplesocket.network

import android.util.Log
import kotlinx.coroutines.*
import java.io.*
import java.net.Socket

class ClientSocket(private val ip: String, private val port: Int) {

    private lateinit var socket: Socket
    private lateinit var job: Job

    fun start() {
        if (::job.isInitialized) if (job.isActive) job.cancel()
        val mes = "hello\n"
        GlobalScope.launch(Dispatchers.IO) {
            try {
                socket = Socket(ip, port)
                Log.d(ClientSocket::class.java.simpleName, "connected")
                val inputStream = BufferedReader(InputStreamReader(socket.getInputStream()))
                val outputStream = PrintWriter(socket.getOutputStream(), true)
                withContext(Dispatchers.IO) {
                    while (isActive) {
                        outputStream.write(mes)
                        outputStream.flush()
                        Log.d(ClientSocket::class.java.simpleName, "sent -> $mes")
                        delay(1000L)
                    }
                }
                withContext(Dispatchers.IO) {
                    while (isActive) {
                        val line = inputStream.readLine()
                        Log.d(ClientSocket::class.java.simpleName, "received -> $line")
                        delay(1000L)
                    }
                }
            } catch (e: IOException) {
                close()
            }
        }
    }

    fun close() {
        if (::job.isInitialized) if (job.isActive) job.cancel()
        try {
            if (::socket.isInitialized) socket.close()
        } catch (e: IOException) {

        }
    }
}