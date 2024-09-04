package com.orbits.queuingsystem.helper.interfaces

import com.google.gson.JsonObject

interface TokenListener {
    fun onTokenReceived(jsonObject: JsonObject?)
}