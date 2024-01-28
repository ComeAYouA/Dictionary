package com.lithium.kotlin.dictionary.features.categories.di

import android.view.LayoutInflater
import com.lithium.kotlin.dictionary.features.categories.sreen.CategoriesFragment
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

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