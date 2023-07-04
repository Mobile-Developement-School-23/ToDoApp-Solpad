package com.example.todoapp.di

import com.example.todoapp.screens.adding.AddingFragment
import dagger.Component

@Component
class AddingFragmentComponent {
    @Component.Factory
    interface Factory{
        fun create():AddingFragmentComponent
    }

}