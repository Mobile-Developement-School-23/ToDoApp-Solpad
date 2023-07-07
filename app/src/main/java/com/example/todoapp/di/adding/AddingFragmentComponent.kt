package com.example.todoapp.di.adding

import com.example.todoapp.screens.adding.AddingFragment
import dagger.Component
import dagger.Subcomponent

@Subcomponent(modules = [AddingFragmentModule::class])
@AddingFramentScope
interface AddingFragmentComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(): AddingFragmentComponent
    }
    fun inject(addingFragment: AddingFragment)
}