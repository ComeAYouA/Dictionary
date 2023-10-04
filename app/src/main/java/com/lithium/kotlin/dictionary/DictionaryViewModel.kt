package com.lithium.kotlin.dictionary

class DictionaryViewModel(){
    private val repository = WordsRepository.get()
    val wordsLiveData = repository.getWords()


}