package com.orbits.queuingsystem.helper

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.orbits.queuingsystem.helper.interfaces.ConnectionStatusListener
import com.orbits.queuingsystem.helper.interfaces.MessageListener
import okhttp3.*
import okio.ByteString

class WebSocketClient(
    private val serverUrl: String,
    private val messageListener: MessageListener,
    private val connectionStatusListener: ConnectionStatusListener?= null
) {

    private var webSocket: WebSocket? = null

    fun connect() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(serverUrl)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                // WebSocket connection established
                println("WebSocket connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                // Received text message
                println("Received message: $text")

                // Notify listener or perform other actions
                val jsonObject = Gson().fromJson(text, JsonObject::class.java)
                messageListener.onMessageJsonReceived(jsonObject)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // Received binary message
                println("Received bytes: ${bytes.hex()}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                // WebSocket is closing
                println("WebSocket closing: $code / $reason")
                webSocket.close(1000, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // WebSocket connection error
                println("WebSocket connection error: ${t.message}")
                Handler(Looper.getMainLooper()).post {
                    connectionStatusListener?.onConnectionBroken(t.message ?: "")
                }
            }
        })
    }

    fun sendMessage(jsonObject : JsonObject) {
        webSocket?.send(jsonObject.toString())
    }

    fun disconnect() {
        println("here is WebSocket disconnected")
        webSocket?.close(1000, null)
    }
}
