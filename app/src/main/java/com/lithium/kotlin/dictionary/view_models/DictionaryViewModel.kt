package com.lithium.kotlin.dictionary.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lithium.kotlin.dictionary.models.Word
import com.lithium.kotlin.dictionary.models.WordsRepository
import java.util.*

class DictionaryViewModel: ViewModel(){
    private val repository = WordsRepository.get()
    var wordsLiveData : LiveData<List<Word>>? = null

    fun load(){
        wordsLiveData = repository.getWords()
    }
    fun load(ids: List<UUID>){
        wordsLiveData = repository.getWordsByIds(ids)
    }

}