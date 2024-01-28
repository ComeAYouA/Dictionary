package com.lithium.kotlin.dictionary.features.dictionary.di

import com.lithium.kotlin.dictionary.features.dictionary.screen.DictionaryFragment
import dagger.BindsInstance
import dagger.Subcomponent

@DictionaryScope
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