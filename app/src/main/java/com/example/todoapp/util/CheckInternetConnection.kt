package com.example.todoapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.todoapp.network.RetrofitRepository
import com.example.todoapp.network.model.GetListItemsNetwork
import com.example.todoapp.network.model.SetItemResponse
import retrofit2.Response
import java.io.IOException

class CheckInternetConnection {


    fun checkingInternetConnectionGet(context: Context,response: Response<GetListItemsNetwork>) =
        if (checkInternet(context)) response
        else throw IOException("Нет интернет соединения")

    fun checkingInternetConnectionSet(context: Context,response: Response<SetItemResponse>) =
        if (checkInternet(context)) response
        else throw IOException("Нет интернет соединения")


    fun checkInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


}