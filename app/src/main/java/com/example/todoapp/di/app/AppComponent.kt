package com.example.todoapp.di.app

import com.example.todoapp.Application
import com.example.todoapp.di.adding.AddingFragmentComponent
import com.example.todoapp.di.main.MainFragmentComponent
import com.example.todoapp.screens.adding.AddingFragmentViewModel
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class])
@AppScope
interface AppComponent {
    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance app:Application
        ): AppComponent
    }

    fun inject(application: Application)
    fun mainFragmentComponent():MainFragmentComponent.Factory
    fun addingFragmentComponent():AddingFragmentComponent.Factory
}