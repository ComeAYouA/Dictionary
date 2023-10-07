package com.lithium.kotlin.dictionary.view_models

import com.lithium.kotlin.dictionary.WordsRepository

class DictionaryViewModel(){
    private val repository = WordsRepository.get()
    val wordsLiveData = repository.getWords()


}