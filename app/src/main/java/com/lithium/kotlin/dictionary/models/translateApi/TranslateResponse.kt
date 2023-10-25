package com.lithium.kotlin.dictionary.models.translateApi

import com.google.gson.annotations.SerializedName

data class TranslateResponse(
    @SerializedName("translatedText")
    val translatedText: String = ""
)