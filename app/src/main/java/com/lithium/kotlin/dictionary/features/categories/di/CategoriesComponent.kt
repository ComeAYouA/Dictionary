package com.lithium.kotlin.dictionary.features.categories.di

import com.lithium.kotlin.dictionary.features.categories.sreen.CategoriesFragment
import dagger.BindsInstance
import dagger.Subcomponent

@CategoriesFragmentScope
@Subcomponent(modules = [CategoriesFragmentModule::class])
interface CategoriesComponent{
    fun inject(categoriesFragment: CategoriesFragment)

    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance fragment: CategoriesFragment
        ): CategoriesComponent
    }

}