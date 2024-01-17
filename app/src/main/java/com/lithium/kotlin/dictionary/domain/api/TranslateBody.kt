package com.lithium.kotlin.dictionary.domain.api

import com.google.gson.annotations.SerializedName

data class TranslateBody(
    @SerializedName("q")
    var q: String = "",
    @SerializedName("source")
    val source: String = "en",
    @SerializedName("target")
    val target: String = "ru",
){

}