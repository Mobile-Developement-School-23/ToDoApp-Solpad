package com.example.todoapp.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context): Interceptor {
    val revisionService = RevisionService(context)
    var token = "glor"

    override fun intercept(chain: Interceptor.Chain): Response {
        var interceptBuilder = chain.request().newBuilder().addHeader("Authorization","Bearer $token" )

        revisionService.getRevisionNetwork().let { revision -> interceptBuilder .addHeader("X-Last-Known-Revision", revision.toString())
        }

        return chain.proceed(interceptBuilder.build())
    }

}