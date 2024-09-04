package com.orbits.queuingsystem.helper

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class NetworkChecker(private val context: Context) {

    private val executor = Executors.newSingleThreadScheduledExecutor()
    private val checkInterval = 10L // seconds
    private var networkListener :  NetworkListener?= null

    fun setNetworkListener(listener: NetworkListener) {
        this.networkListener = listener
    }

    fun start() {
        executor.scheduleWithFixedDelay({
            if (isNetworkAvailable(context)) {
                println("Network is available")
                networkListener?.onSuccess()
            } else {
                // Network is not available, handle reconnection or notify the user
                println("Network is not available")
                networkListener?.onFailure()
            }
        }, 0, checkInterval, TimeUnit.SECONDS)
    }

    fun stop() {
        executor.shutdown()
    }

    @SuppressLint("ServiceCast")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            networkInfo.isConnected
        }
    }

}
