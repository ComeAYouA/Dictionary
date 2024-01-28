package com.lithium.kotlin.dictionary.features.dictionary.di

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.lithium.kotlin.dictionary.features.dictionary.screen.DictionaryFragment
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Qualifier
annotation class DictionaryQualifier
@Module
interface DictionaryFragmentModule {

    companion object {
        @DictionaryQualifier
        @Provides
        fun providesLayoutInflater(fragment: DictionaryFragment): LayoutInflater {
            return fragment.layoutInflater
        }

        @DictionaryQualifier
        @Provides
        fun providesContext(fragment: DictionaryFragment): Context {
            return fragment.requireContext()
        }

        @DictionaryQualifier
        @Provides
        fun providesCallBacks(fragment: DictionaryFragment): DictionaryFragment.CallBacks?{
            return fragment.callBacks
        }
        @Provides
        fun providesFragmentManager(fragment: DictionaryFragment): FragmentManager {
            return fragment.requireFragmentManager()
        }
    }
}