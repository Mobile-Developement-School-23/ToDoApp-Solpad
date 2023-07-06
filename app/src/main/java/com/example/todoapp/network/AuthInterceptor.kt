package com.example.todoapp.network

import android.content.Context
import com.example.todoapp.di.app.AppScope
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@AppScope
class AuthInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder =
            chain.request().newBuilder().addHeader("Authorization", "Bearer glor")

        return chain.proceed(requestBuilder.build())
    }

}