package com.lithium.kotlin.dictionary.presentation.word

import android.util.Log
import androidx.lifecycle.*
import com.lithium.kotlin.dictionary.domain.localdatabasemodels.Category
import com.lithium.kotlin.dictionary.domain.localdatabasemodels.Word
import com.lithium.kotlin.dictionary.data.repositories.WordsRepository
import com.lithium.kotlin.dictionary.data.repositories.translateApi.TranslateApi
import com.lithium.kotlin.dictionary.domain.translateapimodels.TranslateBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*

class EditDictionaryViewModel : ViewModel(){

    private val repository: WordsRepository = WordsRepository.get()
    private val _word: MutableStateFlow<Word> = MutableStateFlow(Word())
    val word: StateFlow<Word> = _word.asStateFlow()

    private val _translate: MutableStateFlow<String> = MutableStateFlow("")
    val translate: StateFlow<String> = _translate.asStateFlow()

    private val requestsContext = SupervisorJob() + Dispatchers.IO

    private val translateApi = TranslateApi.get()

    fun translateRequest(word: String) {


        requestsContext.cancelChildren()

        viewModelScope.launch(requestsContext) {

            val body = TranslateBody(q = word)
            delay(3000)

            if (isActive) {
                Log.d("request", body.toString())
                try{
                    val result = translateApi.translateApi.translate(body)
                    _translate.value = result.translatedText
                }catch (e: Exception){
                    Log.d("request", e.toString())
                }
            }
        }
    }

    var categories = listOf<Category>()
    var categoriesNames = listOf<String>()

    fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories().flowOn(Dispatchers.IO).collect { _categories ->
                categories = _categories
                categoriesNames = categories.map { it.name }
            }
        }
    }

    fun loadWord(wordId: UUID){
        viewModelScope.launch {
            repository.getWord(wordId).flowOn(Dispatchers.IO).collect{
                    _word.value = it?: Word()
                }
            }
    }

    fun saveWord(word: Word){
        viewModelScope.launch(Dispatchers.IO) {
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
    }

    fun addWord(word: Word){
        viewModelScope.launch(Dispatchers.IO){
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

}