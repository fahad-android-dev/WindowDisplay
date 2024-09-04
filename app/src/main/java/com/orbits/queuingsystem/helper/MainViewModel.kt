package com.orbits.queuingsystem.helper

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.orbits.queuingsystem.helper.interfaces.ConnectionListener
import com.orbits.queuingsystem.helper.interfaces.ConnectionStatusListener
import com.orbits.queuingsystem.helper.interfaces.CounterListener
import com.orbits.queuingsystem.helper.interfaces.MessageListener
import com.orbits.queuingsystem.helper.interfaces.TokenListener
import kotlin.random.Random

class MainViewModel : ViewModel() , MessageListener, ConnectionStatusListener {

    val gson = Gson()
    var isConnected: Boolean = false

    var webSocketClient: WebSocketClient? = null
    var dataModel: JsonObject? = null
    var dataList = ArrayList<CounterListDataModel>()
    private var tokenListener: TokenListener? = null
    private var counterListener :  CounterListener?= null
    private var connectionListener :  ConnectionListener?= null
    var displayId = ""



    fun setTokenListener(listener: TokenListener) {
        this.tokenListener = listener
    }

    fun setCounterListener(listener: CounterListener) {
        this.counterListener = listener
    }

    fun setConnectionListener(listener: ConnectionListener) {
        this.connectionListener = listener
    }

    fun connectWebSocket(ipAddress: String, port: String) {
        webSocketClient = WebSocketClient("ws://$ipAddress:$port",this,this)
        webSocketClient?.connect()

        println("Here is websocket connected")

        isConnected = true
        val jsonObject = JsonObject()
        jsonObject.addProperty("displayConnection", "displayConnection")


        if (isConnected) {
            webSocketClient?.sendMessage(jsonObject)
            Log.d("WebSocketViewModel", "Message sent: $jsonObject")
        } else {
            Log.e("WebSocketViewModel", "WebSocket is not connected, cannot send message.")
        }

    }

    fun sendMessage(message: JsonObject) {
        webSocketClient?.sendMessage(message)
    }


    override fun onCleared() {
        super.onCleared()
        isConnected = false
        webSocketClient?.disconnect()
    }


    override fun onMessageJsonReceived(jsonObject: JsonObject) {

        println("here is msg $jsonObject")


        /*dataModel = jsonObject
        println("here is data with token $dataModel")*/

        if (jsonObject.has("reconnected")){
            connectionListener?.onConnectionRestarted()

        }else {
            if (!jsonObject.has("displayConnected")){
                if (jsonObject.has("transaction")){
                    dataModel = jsonObject
                    println("here is data with token $dataModel")
                    tokenListener?.onTokenReceived(dataModel)
                    counterListener?.onCounterReceived(dataModel)
                }else {
                    val counters = parseJsonData(jsonObject)
                    println("Parsed items: $counters")
                    displayId = jsonObject.get("displayId").asString
                    dataList.clear()
                    dataList.addAll(counters)
                }
            }
        }

    }

    fun sendConnectionMessage(counterType :String,counterId: String,serviceId: String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("keypadCounterType", counterType)
        jsonObject.addProperty("counterId", counterId)
        jsonObject.addProperty("message", "ConnectionWithDisplay")
        jsonObject.addProperty("serviceId", serviceId)
        jsonObject.addProperty("displayId", displayId)

        webSocketClient?.sendMessage(jsonObject)
        Log.d("Connection Message", "Sending message: $jsonObject")
    }

    fun sendReConnectionMessage(counterType :String,counterId: String,serviceId: String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("keypadCounterType", counterType)
        jsonObject.addProperty("counterId", counterId)
        jsonObject.addProperty("Reconnection", "Reconnection")
        jsonObject.addProperty("serviceId", serviceId)
        jsonObject.addProperty("displayId", displayId)

        webSocketClient?.sendMessage(jsonObject)
        Log.d("Connection Message", "Sending message: $jsonObject")
    }


    fun sendNewMessage(transaction: TransactionDataModel ?= null,counterId: String) {
        val jsonObject = JsonObject()
        val jsonModel = gson.toJson(transaction)
        jsonObject.add("transaction",  gson.fromJson(jsonModel, JsonObject::class.java))
        jsonObject.addProperty("displayId", "D${counterId}")


        webSocketClient?.sendMessage(jsonObject)
        Log.d("FragmentTwo", "Sending message: $jsonObject")
    }

    var counter = 1

    fun generateCustomId(): String {
        val randomTwoDigitNumber = Random.nextInt(10, 100) // Generates a random number between 10 and 99
        return "D$randomTwoDigitNumber"
    }

    override fun onConnectionBroken(message: String) {
        connectionListener?.onConnectionBroken(message)
    }
}