package com.lg.domain.githublist.usecase

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

class InternetStateUseCase(private val context: Context) {

    fun isDeviceConnectedToNetwork(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissionAboveAndroidM(connectivityManager)
        } else {
            checkConnectionBelowAndroidM(connectivityManager)
        }
    }

    private fun checkConnectionBelowAndroidM(
        connectivityManager: ConnectivityManager
    ): Boolean {
        var result = false
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissionAboveAndroidM(
        connectivityManager: ConnectivityManager
    ): Boolean {
        val networkCapabilities =
            connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities)
                ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}