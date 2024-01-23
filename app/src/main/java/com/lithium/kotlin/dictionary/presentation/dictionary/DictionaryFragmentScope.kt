package com.lithium.kotlin.dictionary.presentation.dictionary

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import com.lithium.kotlin.dictionary.presentation.dictionary.screen.DictionaryFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Qualifier
import javax.inject.Scope

@Scope
annotation class DictionaryFragmentScope

@DictionaryFragmentScope
@Subcomponent(modules = [DictionaryFragmentModule::class])
interface DictionaryComponent{
    fun inject(dictionaryFragment: DictionaryFragment)

    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance fragment: DictionaryFragment
        ): DictionaryComponent
    }
}

@Qualifier
annotation class DictionaryQualifier
@Module
interface DictionaryFragmentModule {

    companion object {
        @DictionaryQualifier
        @Provides
        fun providesLayoutInflater(fragment: DictionaryFragment): LayoutInflater{
            return fragment.layoutInflater
        }

        @DictionaryQualifier
        @Provides
        fun providesContext(fragment: DictionaryFragment): Context{
            return fragment.requireContext()
        }

        @DictionaryQualifier
        @Provides
        fun providesCallBacks(fragment: DictionaryFragment): DictionaryFragment.CallBacks?{
            return fragment.callBacks
        }
    }
}