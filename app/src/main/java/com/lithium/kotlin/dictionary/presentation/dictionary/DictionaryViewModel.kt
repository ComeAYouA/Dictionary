package com.lithium.kotlin.dictionary.presentation.dictionary

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.data.di.RepositoryModule
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.data.repository.WordsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class DictionaryViewModel @Inject constructor(
    private val repository: WordsRepositoryImpl
): ViewModel() {

    private val _words: MutableStateFlow<List<Word>> = MutableStateFlow(listOf())
    val words: StateFlow<List<Word>> = _words.asStateFlow()

    private val _tempWords: MutableStateFlow<List<Word>> = MutableStateFlow(listOf())
    val tempWords: StateFlow<List<Word>> = _tempWords.asStateFlow()

    fun load() {
        viewModelScope.launch {
            repository.getWords().flowOn(Dispatchers.IO).collect {
                _words.value = it
                _tempWords.value = words.value
            }
        }
    }
    fun load(ids: List<UUID>) {
        viewModelScope.launch {
            repository.getWordsByIds(ids).flowOn(Dispatchers.IO).collect {
                _words.value = it
                _tempWords.value = words.value
            }
        }
    }

    fun search(input: String) {
        viewModelScope.launch {

            if (input.isNotEmpty()) {
                _tempWords.value = _words.value.fold(mutableListOf()) { acc, w ->

                    if (w.sequence.contains(input)) {
                        acc.add(w)
                    }

                    acc
                }
            }else{
                _tempWords.value = words.value
            }
        }
    }

}
