package com.lithium.kotlin.dictionary.view_models

import androidx.lifecycle.LiveData
import com.lithium.kotlin.dictionary.models.Word
import com.lithium.kotlin.dictionary.models.WordsRepository
import java.util.*

class DictionaryViewModel(){
    private val repository = WordsRepository.get()
    var wordsLiveData : LiveData<List<Word>>? = null

    fun load(){
        wordsLiveData = repository.getWords()
    }
    fun load(ids: List<UUID>){
        wordsLiveData = repository.getWordsByIds(ids)
    }

}