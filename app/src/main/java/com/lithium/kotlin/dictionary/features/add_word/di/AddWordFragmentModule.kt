package com.lithium.kotlin.dictionary.features.add_word.di

import android.view.LayoutInflater
import com.lithium.kotlin.dictionary.databinding.FragmentWordBinding
import com.lithium.kotlin.dictionary.features.add_word.screen.AddWordFragment
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Qualifier
annotation class AddWordQualifier

@Module
interface AddWordFragmentModule{

    companion object{
        @Provides
        fun provideAddWordFragmentLayoutInflater(fragment: AddWordFragment): LayoutInflater {
            return fragment.layoutInflater
        }

        @Provides
        fun provideAddWordFragmentCallBacks(fragment: AddWordFragment): AddWordFragment.CallBacks? {
            return fragment.callBacks
        }

        @Provides
        fun provideAddWordFragmentBinding(fragment: AddWordFragment): FragmentWordBinding {
            return fragment.binding
        }
    }
}
