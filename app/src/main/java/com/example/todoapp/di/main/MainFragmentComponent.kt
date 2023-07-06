package com.example.todoapp.di.main

import com.example.todoapp.di.adding.AddingFragmentComponent
import com.example.todoapp.screens.main.MainFragment
import dagger.Component
import dagger.Subcomponent

@Subcomponent(modules = [MainFragmentModule::class])
interface MainFragmentComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(): MainFragmentComponent
    }
    fun inject(mainFragment: MainFragment)
}