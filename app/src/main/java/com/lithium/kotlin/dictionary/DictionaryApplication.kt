package com.lithium.kotlin.dictionary

import android.app.Application
import com.lithium.kotlin.dictionary.data.repositories.translateApi.TranslateApi
import com.lithium.kotlin.dictionary.data.repositories.WordsRepository

class DictionaryApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        WordsRepository.initialize(this)
        TranslateApi.initialize(this)
    }
}