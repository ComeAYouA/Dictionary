package com.lithium.kotlin.dictionary.presentation.di

import com.lithium.kotlin.dictionary.presentation.dictionary.DictionaryFragment
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class DictionaryFragmentScope

@DictionaryFragmentScope
@Subcomponent
interface DictionaryFragmentComponent{
    fun inject(dictionaryFragment: DictionaryFragment)
}