package com.orbits.queuingsystem.helper.interfaces

import android.view.View

interface AlertDialogInterface {
    fun onYesClick() {}
    fun onNoClick() {}
    fun onConnectionConfirm(ipAddress: String,port: String) {}
    fun onCounterSelected(counterId: String,counterType: String,serviceId:String) {}
}