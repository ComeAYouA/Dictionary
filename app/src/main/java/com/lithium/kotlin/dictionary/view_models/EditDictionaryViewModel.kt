package com.lithium.kotlin.dictionary.view_models

import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.lithium.kotlin.dictionary.models.*
import com.lithium.kotlin.dictionary.models.translateApi.TranslateApi
import com.lithium.kotlin.dictionary.models.translateApi.TranslateBody
import com.lithium.kotlin.dictionary.models.translateApi.TranslateResponse
import retrofit2.Call
import java.util.*

class EditDictionaryViewModel : ViewModel(){
    private val repository: WordsRepository = WordsRepository.get()
    private val wordIdLiveData = MutableLiveData<UUID>()
    private val translateApi = TranslateApi.get()

    fun translateRequest(word: String): Call<TranslateResponse> {
        val body = TranslateBody(q = word)
        Log.d("Trans", Gson().toJson(body))
        return translateApi.translateApi.translate(body)
    }

    val wordLiveData : LiveData<Word?> =
        Transformations.switchMap(wordIdLiveData){ wordId ->
            repository.getWord(wordId)
        }

    var categories = listOf<Category>()
    var categoriesNames = listOf<String>()

    fun loadCategories(){
        repository.getCategories().observeForever { _categories ->
            categories = _categories
            categoriesNames = categories.map { it.name }

        }
    }

    fun loadWord(wordId: UUID){
        wordIdLiveData.value = wordId
    }

    fun saveWord(word: Word){
        repository.updateWord(word)

        word.categories.forEach {
            if (!categoriesNames.contains(it)) {
                repository.addCategory(Category(it, mutableSetOf(word.id)))
            }
        }

        categories.forEach{
            if (word.categories.contains(it.name)){
                if (!it.ids.contains(word.id)){
                    it.ids.add(word.id)
                    repository.updateCategory(it)
                }
            }
        }
    }

    fun addWord(word: Word){
        repository.addWord(word)
        word.categories.forEach {
            if (!categoriesNames.contains(it)) {
                repository.addCategory(Category(it, mutableSetOf(word.id)))
            }
        }

        categories.forEach{
            if (word.categories.contains(it.name)){
                if (!it.ids.contains(word.id)){
                    it.ids.add(word.id)
                    repository.updateCategory(it)
                }
            }
        }
    }

}