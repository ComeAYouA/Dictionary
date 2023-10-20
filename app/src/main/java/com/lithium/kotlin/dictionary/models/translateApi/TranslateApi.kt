package com.lithium.kotlin.dictionary.models.translateApi

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TranslateApi {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://libretranslate.de/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val translateApi: LibreTranslateApi = retrofit.create(LibreTranslateApi::class.java)

    companion object {
        private var INSTANCE: TranslateApi? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TranslateApi()
            }
        }
        fun get(): TranslateApi {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}