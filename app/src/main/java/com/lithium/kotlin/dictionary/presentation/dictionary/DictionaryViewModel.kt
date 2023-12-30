package com.lithium.kotlin.dictionary.presentation.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.domain.localdatabasemodels.Word
import com.lithium.kotlin.dictionary.data.repositories.WordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*

class DictionaryViewModel: ViewModel() {
    private val repository = WordsRepository.get()
    private val _words: MutableStateFlow<List<Word>> = MutableStateFlow(listOf())
    val words: StateFlow<List<Word>> = _words.asStateFlow()

    fun load() {
        viewModelScope.launch {
            repository.getWords().flowOn(Dispatchers.IO).collect {
                _words.value = it
            }
        }
    }
    fun load(ids: List<UUID>) {
        viewModelScope.launch {
            repository.getWordsByIds(ids).flowOn(Dispatchers.IO).collect {
                _words.value = it
            }
        }
    }

}
