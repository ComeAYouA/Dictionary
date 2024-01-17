package com.lithium.kotlin.dictionary.domain.api

import com.google.gson.annotations.SerializedName

data class TranslateResponse(
    @SerializedName("translatedText")
    val translatedText: String = ""
)