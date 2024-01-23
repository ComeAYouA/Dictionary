package com.lithium.kotlin.dictionary.presentation.word.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.data.repository.WordsRepositoryImpl
import com.lithium.kotlin.dictionary.domain.usecases.AddWordToDictionaryUseCase
import com.lithium.kotlin.dictionary.domain.api.TranslateApi
import com.lithium.kotlin.dictionary.domain.api.TranslateBody
import com.lithium.kotlin.dictionary.domain.models.Category
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import com.lithium.kotlin.dictionary.presentation.word.AddWordFragmentScope
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
import javax.inject.Inject

@AddWordFragmentScope
class AddWordViewModel @Inject constructor(
    private val repository: WordDao,
    private val translateApi: TranslateApi,
    private val addWordToDictionaryUseCase: AddWordToDictionaryUseCase
): ViewModel(){

    init {
        Log.d("myTag", "AddWord ViewModel init")
    }

    private val _word: MutableStateFlow<Word> = MutableStateFlow(Word())
    val word: StateFlow<Word> = _word.asStateFlow()

    private val _translate: MutableStateFlow<String> = MutableStateFlow("")
    val translate: StateFlow<String> = _translate.asStateFlow()

    private val translationRequestsContext = SupervisorJob() + Dispatchers.IO

    fun translateRequest(word: String) {
        translationRequestsContext.cancelChildren()

        viewModelScope.launch(translationRequestsContext) {
            val body = TranslateBody(q = word)
            delay(3000)

            if (isActive) {
                try{
                    val result = translateApi.translate(body)
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

//    fun loadWord(wordId: UUID){
//        viewModelScope.launch {
//            repository.getWord(wordId).flowOn(Dispatchers.IO).collect{
//                    _word.value = it?: Word()
//                }
//            }
//    }

//    fun saveWord(word: Word){
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.updateWord(word)
//
//            word.categories.forEach {
//                if (!categoriesNames.contains(it)) {
//                    repository.addCategory(Category(it, mutableSetOf(word.id)))
//                }
//            }
//
//            categories.forEach{
//                if (word.categories.contains(it.name)){
//                    if (!it.ids.contains(word.id)){
//                        it.ids.add(word.id)
//                        repository.updateCategory(it)
//                    }
//                }
//            }
//        }
//    }

    fun addWord(word: Word){
        viewModelScope.launch{
            addWordToDictionaryUseCase(word)
        }
    }
}