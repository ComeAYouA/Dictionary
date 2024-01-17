package com.lithium.kotlin.dictionary.presentation.di

import com.lithium.kotlin.dictionary.presentation.categories.CategoriesFragment
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class CategoriesFragmentScope

@CategoriesFragmentScope
@Subcomponent
interface CategoriesFragmentComponent{
    fun inject(categoriesFragment: CategoriesFragment)
}