package com.lithium.kotlin.dictionary.features.add_word.di

import com.lithium.kotlin.dictionary.data.api.ApiScope
import com.lithium.kotlin.dictionary.data.di.NetworkModule
import com.lithium.kotlin.dictionary.features.add_word.screen.AddWordFragment
import dagger.BindsInstance
import dagger.Subcomponent

@AddWordScope
@ApiScope
@Subcomponent(modules = [NetworkModule::class, AddWordFragmentModule::class])
interface AddWordComponent {
    fun inject(fragment: AddWordFragment)

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance fragment: AddWordFragment): AddWordComponent
    }
}