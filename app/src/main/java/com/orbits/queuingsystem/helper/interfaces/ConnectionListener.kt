package com.orbits.queuingsystem.helper.interfaces

interface ConnectionListener {
    fun onConnectionBroken(message: String)
    fun onConnectionRestarted()
}
