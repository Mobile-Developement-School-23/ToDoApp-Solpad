package com.example.todoapp.di.data

import android.content.Context
import com.example.todoapp.network.RevisionInterceptor
import com.example.todoapp.di.app.AppScope
import com.example.todoapp.network.AuthInterceptor
import com.example.todoapp.network.RevisionService
import com.example.todoapp.network.TodoApiRequest
import com.example.todoapp.util.BASEURL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    @AppScope
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        revisionInterceptor: RevisionInterceptor
    ):OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(revisionInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }
    @Provides
    @AppScope
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient).build()
    }
    @Provides
    @AppScope
    fun provideTodoApiRequest(retrofit: Retrofit): TodoApiRequest {
        return retrofit.create(TodoApiRequest::class.java)
    }
    @Provides
    @AppScope
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @AppScope
    fun provideRevisionService(context:Context): RevisionService {
        return RevisionService(context)
    }

}