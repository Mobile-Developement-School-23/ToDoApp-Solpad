package com.example.todoapp.network

import android.content.Context
import com.example.todoapp.util.BASEURL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitRepository {

    companion object {
        @Volatile
        private var instance: TodoApiRequest? = null
        private val lock = Any()

        fun getRetrofit(context: Context) = instance ?: synchronized(lock) {
            instance ?: createRetrofit(context).also { instance = it }
        }

        private fun createRetrofit(context: Context): TodoApiRequest {
            return Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build().create(TodoApiRequest::class.java)
        }

        private fun okhttpClient(context: Context): OkHttpClient {
            val logging = HttpLoggingInterceptor().also {
                it.setLevel(HttpLoggingInterceptor.Level.BODY)
            }
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(AuthInterceptor(context))
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()
        }
    }
}
