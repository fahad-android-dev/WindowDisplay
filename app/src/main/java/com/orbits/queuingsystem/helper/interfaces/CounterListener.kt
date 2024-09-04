package com.orbits.queuingsystem.helper.interfaces

import com.google.gson.JsonObject

interface CounterListener {
    fun onCounterReceived(jsonObject: JsonObject?)

}