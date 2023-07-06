package com.example.todoapp.di.app

import android.content.Context
import android.net.ConnectivityManager
import com.example.todoapp.Application
import com.example.todoapp.di.data.DatabaseModule
import com.example.todoapp.di.data.NetworkModule
import com.example.todoapp.di.adding.AddingFragmentComponent
import com.example.todoapp.di.main.MainFragmentComponent
import dagger.Module
import dagger.Provides

@Module(
    includes = [NetworkModule::class, DatabaseModule::class],
    subcomponents = [MainFragmentComponent::class,AddingFragmentComponent::class]
)
class AppModule {

    @Provides
    @AppScope
    fun provideContext(app:Application):Context{
        return app.applicationContext
    }
    @Provides
    @AppScope
    fun provideConnectivityManager(appContext: Context): ConnectivityManager {
        return appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}