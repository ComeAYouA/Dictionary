package com.lithium.kotlin.dictionary.data.repositories.translateApi

import com.lithium.kotlin.dictionary.domain.translateapimodels.TranslateBody
import com.lithium.kotlin.dictionary.domain.translateapimodels.TranslateResponse
import retrofit2.http.*

interface LibreTranslateApi {

    @Headers("accept: application/json")
    @POST("translate")
    suspend fun translate(@Body translateBody: TranslateBody): TranslateResponse
}