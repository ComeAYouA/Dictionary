package com.lithium.kotlin.dictionary

import android.app.Application
import com.lithium.kotlin.dictionary.models.translateApi.TranslateApi
import com.lithium.kotlin.dictionary.models.WordsRepository

class DictionaryApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        WordsRepository.initialize(this)
        TranslateApi.initialize(this)
    }
}