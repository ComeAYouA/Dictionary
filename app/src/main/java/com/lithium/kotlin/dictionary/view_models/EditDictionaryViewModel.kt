package com.lithium.kotlin.dictionary.view_models

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.lithium.kotlin.dictionary.Category
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
    var categoriesNames = listOf<String>()
    fun loadCategories(){
        repository.getCategories().observeForever { categories ->
            categoriesNames = categories.map { it.name }
        }
    }
    fun loadWord(wordId: UUID){
        wordIdLiveData.value = wordId
    }
    fun saveWord(word: Word){
        repository.updateWord(word)
    }
    fun addWord(word: Word, lifecycleOwner: LifecycleOwner){
        repository.addWord(word)
        repository.getCategories().observeForever{ categories ->
            word.categories.forEach{ category ->
                if (!categoriesNames.contains(category)){
                    repository.addCategory(Category(category, mutableSetOf(word.id.toString())))
                }
            }
        }
    }
}