package com.lithium.kotlin.dictionary.presentation.di

import com.lithium.kotlin.dictionary.data.api.ApiScope
import com.lithium.kotlin.dictionary.data.di.NetworkModule
import com.lithium.kotlin.dictionary.presentation.word.AddWordFragment
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class AddWordFragmentScope

@AddWordFragmentScope
@ApiScope
@Subcomponent(modules = [NetworkModule::class])
interface AddWordComponent {
    fun inject(fragment: AddWordFragment)

}