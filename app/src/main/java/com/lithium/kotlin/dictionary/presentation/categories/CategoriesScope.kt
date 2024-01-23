package com.lithium.kotlin.dictionary.presentation.categories

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import com.lithium.kotlin.dictionary.presentation.categories.sreen.CategoriesFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Qualifier
import javax.inject.Scope

@Scope
annotation class CategoriesFragmentScope

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

@Qualifier
annotation class CategoriesFragmentQualifier
@Module
interface CategoriesFragmentModule{

    companion object{

        @CategoriesFragmentQualifier
        @Provides
        fun provideCategoriesFragmentCallBacks(fragment: CategoriesFragment): CategoriesFragment.CallBacks? {
            return fragment.callBacks
        }

        @CategoriesFragmentQualifier
        @Provides
        fun provideCategoriesFragmentLayoutInflater(fragment: CategoriesFragment): LayoutInflater {
            return fragment.layoutInflater
        }

    }
}

