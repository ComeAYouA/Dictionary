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
import com.lithium.kotlin.dictionary.domain.usecases.TranslateEnteredWordUseCase
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
    private val addWordToDictionaryUseCase: AddWordToDictionaryUseCase,
    private val translateEnteredWordUseCase: TranslateEnteredWordUseCase
): ViewModel(){

    init {
        Log.d("myTag", "AddWord ViewModel init")
    }

    private val _word: MutableStateFlow<Word> = MutableStateFlow(Word())
    val word: StateFlow<Word> = _word.asStateFlow()

    private val _translate: MutableStateFlow<String> = MutableStateFlow("")
    val translate: StateFlow<String> = _translate.asStateFlow()

    var iconPath = ""

    fun translateRequest(word: String) {
        viewModelScope.launch {
            _translate.value = translateEnteredWordUseCase(word)
        }
    }
    fun addWord(word: Word){
        viewModelScope.launch{
            addWordToDictionaryUseCase(word)
        }
    }
}