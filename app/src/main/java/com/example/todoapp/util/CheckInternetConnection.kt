package com.example.todoapp.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.todoapp.network.model.GetListItemsNetwork
import com.example.todoapp.network.model.SetItemResponse
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


class CheckInternetConnection @Inject constructor(private val connectivityManager: ConnectivityManager){


    fun checkInternetGet(response: Response<GetListItemsNetwork>) =
        if (checkInternet()) response
        else throw IOException("Нет интернет соединения")

    fun checkInternetSet(response: Response<SetItemResponse>) =
        if (checkInternet()) response
        else throw IOException("Нет интернет соединения")


    fun checkInternet (): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


}