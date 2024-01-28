package com.lithium.kotlin.dictionary.features.dictionary.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import com.lithium.kotlin.dictionary.domain.usecases.GetWordsFlowUseCase
import com.lithium.kotlin.dictionary.domain.usecases.SearchWordsUseCase
import com.lithium.kotlin.dictionary.features.dictionary.di.DictionaryScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@DictionaryScope
class DictionaryViewModel @Inject constructor(
    private val searchWordsUseCase: SearchWordsUseCase,
    private val getWordsFlowUseCase: GetWordsFlowUseCase
): ViewModel() {

    init {
        Log.d("myTag", "DictionaryViewModel init")
    }

    private val _words: MutableStateFlow<List<Word>> = MutableStateFlow(listOf())
    val words: StateFlow<List<Word>> = _words.asStateFlow()

    private val _tempWords: MutableStateFlow<List<Word>> = MutableStateFlow(listOf())
    val tempWords: StateFlow<List<Word>> = _tempWords.asStateFlow()

    fun loadDictionary() {
        viewModelScope.launch {
            getWordsFlowUseCase().collect {
                _words.value = it
                _tempWords.value = words.value
            }
        }
    }

    fun searchWord(input: String) {
        viewModelScope.launch {
            if (input.isNotEmpty()) {
                _tempWords.value = searchWordsUseCase(input, _words.value)
            }else{
                _tempWords.value = words.value
            }
        }
    }

}