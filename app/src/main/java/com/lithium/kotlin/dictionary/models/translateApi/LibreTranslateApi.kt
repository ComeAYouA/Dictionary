package com.lithium.kotlin.dictionary.models.translateApi

import retrofit2.Call
import retrofit2.http.*

interface LibreTranslateApi {

    @Headers("accept: application/json")
    @POST("translate")
    fun translate(@Body translateBody: TranslateBody): Call<TranslateResponse>
}