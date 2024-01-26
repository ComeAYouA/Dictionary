package com.lithium.kotlin.dictionary.presentation.word

import android.view.LayoutInflater
import com.lithium.kotlin.dictionary.data.api.ApiScope
import com.lithium.kotlin.dictionary.data.di.NetworkModule
import com.lithium.kotlin.dictionary.databinding.FragmentWordBinding
import com.lithium.kotlin.dictionary.presentation.word.screen.AddWordFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class AddWordFragmentScope

@AddWordFragmentScope
@ApiScope
@Subcomponent(modules = [NetworkModule::class, AddWordFragmentModule::class])
interface AddWordComponent {
    fun inject(fragment: AddWordFragment)

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance fragment: AddWordFragment): AddWordComponent
    }
}

@Module
interface AddWordFragmentModule{

    companion object{
        @Provides
        fun provideAddWordFragmentLayoutInflater(fragment: AddWordFragment): LayoutInflater{
            return fragment.layoutInflater
        }

        @Provides
        fun provideAddWordFragmentCallBacks(fragment: AddWordFragment): AddWordFragment.CallBacks? {
            return fragment.callBacks
        }

        @Provides
        fun provideAddWordFragmentBinding(fragment: AddWordFragment): FragmentWordBinding{
            return fragment.binding
        }
    }
}