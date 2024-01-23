package com.lithium.kotlin.dictionary.data.di

import com.lithium.kotlin.dictionary.data.api.TranslateApiImpl
import com.lithium.kotlin.dictionary.domain.api.TranslateApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
interface NetworkModule {

    companion object {

        @Provides
        fun provideTranslateRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://libretranslate.de/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        fun provideTranslateApi(translateApiImpl: TranslateApiImpl): TranslateApi {
            return translateApiImpl.translateApi
        }
    }
}