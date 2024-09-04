package com.orbits.queuingsystem.helper.interfaces

interface ConnectionStatusListener {
    fun onConnectionBroken(message: String)
}
