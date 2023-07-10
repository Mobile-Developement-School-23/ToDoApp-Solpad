package com.example.todoapp.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.todoapp.network.model.GetListItemsNetwork
import com.example.todoapp.network.model.SetItemResponse
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject


class CheckInternetConnection @Inject constructor(private val connectivityManager: ConnectivityManager){

    fun checkerResponse(response: Response<SetItemResponse>): Boolean {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return true
            else throw UnknownHostException("Тело запроса - null")
        } else throw UnknownHostException(response.message())
    }
    fun checkerGetResponse(response: Response<GetListItemsNetwork>): Boolean {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return true
            else throw UnknownHostException("Тело запроса - null")
        } else throw UnknownHostException(response.message())
    }
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