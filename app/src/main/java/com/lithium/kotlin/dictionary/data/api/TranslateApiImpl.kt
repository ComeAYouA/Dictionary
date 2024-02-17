package com.lithium.kotlin.dictionary.data.api

import android.content.Context
import android.util.Log
import com.lithium.kotlin.dictionary.domain.api.TranslateApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Scope
import javax.inject.Singleton
@Scope
annotation class ApiScope

@ApiScope
class TranslateApiImpl @Inject constructor(retrofit: Retrofit) {
    val translateApi: TranslateApi = retrofit.create(TranslateApi::class.java)
}