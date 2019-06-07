package com.trials.samplesocket

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var clientSocket: ClientSocket
    private lateinit var serverSocket: MyServerSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        val port = 7070
        clientSocket = ClientSocket("192.168.3.3", port)
        serverSocket = MyServerSocket(port)
        serverSocket.start()
        clientSocket.start()
    }

    override fun onPause() {
        super.onPause()
        serverSocket.close()
        clientSocket.close()
    }
}
