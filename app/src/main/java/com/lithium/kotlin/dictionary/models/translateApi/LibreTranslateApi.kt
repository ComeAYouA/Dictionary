package com.lithium.kotlin.dictionary.models.translateApi

import android.util.JsonReader
import android.util.JsonWriter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.lithium.kotlin.dictionary.models.TranslateBody
import com.lithium.kotlin.dictionary.models.TranslateResponse
import retrofit2.Call
import retrofit2.http.*

interface LibreTranslateApi {

    @Headers("accept: application/json")
    @POST("translate")
    fun translate(@Body translateBody: TranslateBody): Call<TranslateResponse>
}