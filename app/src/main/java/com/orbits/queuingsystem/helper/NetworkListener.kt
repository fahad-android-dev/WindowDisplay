package com.orbits.queuingsystem.helper

import com.google.gson.JsonObject

interface NetworkListener {
    fun onSuccess()
    fun onFailure()
}