package com.lithium.kotlin.dictionary.domain.api

import retrofit2.http.*

interface TranslateApi {

    @Headers("accept: application/json")
    @POST("translate")
    suspend fun translate(@Body translateBody: TranslateBody): TranslateResponse
}