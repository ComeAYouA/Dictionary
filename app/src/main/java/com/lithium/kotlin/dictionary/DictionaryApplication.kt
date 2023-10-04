package com.lithium.kotlin.dictionary

import android.app.Application

class DictionaryApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        WordsRepository.initialize(this)
    }
}