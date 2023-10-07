package com.lithium.kotlin.dictionary.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.lithium.kotlin.dictionary.Word
import com.lithium.kotlin.dictionary.WordsRepository
import java.util.*

class EditDictionaryViewModel(){
    private val repository: WordsRepository = WordsRepository.get()
    private val wordIdLiveData = MutableLiveData<UUID>()

    val wordLiveData : LiveData<Word?> =
        Transformations.switchMap(wordIdLiveData){ wordId ->
            repository.getWord(wordId)
        }
    fun loadWord(wordId: UUID){
        wordIdLiveData.value = wordId
    }
    fun saveWord(word: Word){
        repository.updateWord(word)
    }
    fun addWord(word: Word){
        repository.addWord(word)
    }
}