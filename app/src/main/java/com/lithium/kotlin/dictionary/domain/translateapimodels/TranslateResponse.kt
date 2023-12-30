package com.lithium.kotlin.dictionary.domain.translateapimodels

import com.google.gson.annotations.SerializedName

data class TranslateResponse(
    @SerializedName("translatedText")
    val translatedText: String = ""
)