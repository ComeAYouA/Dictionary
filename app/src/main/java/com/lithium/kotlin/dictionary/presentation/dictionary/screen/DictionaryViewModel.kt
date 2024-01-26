package com.lithium.kotlin.dictionary.presentation.dictionary.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import com.lithium.kotlin.dictionary.domain.usecases.SearchWordsUseCase
import com.lithium.kotlin.dictionary.presentation.dictionary.DictionaryFragmentScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@DictionaryFragmentScope
class DictionaryViewModel @Inject constructor(
    private val repository: WordDao,
    private val searchWordsUseCase: SearchWordsUseCase
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
            repository.getWords().flowOn(Dispatchers.IO).collect {
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