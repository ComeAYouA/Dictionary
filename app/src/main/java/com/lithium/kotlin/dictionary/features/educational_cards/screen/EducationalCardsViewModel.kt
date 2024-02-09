package com.lithium.kotlin.dictionary.features.educational_cards.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.usecases.LoadWordsEducationalListUseCase
import com.lithium.kotlin.dictionary.domain.usecases.SaveWordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EducationalCardsViewModel @Inject constructor(
    private val loadWordsEducationalListUseCase: LoadWordsEducationalListUseCase,
    private val saveWordUseCase: SaveWordUseCase
): ViewModel() {

    private val _words: MutableStateFlow<List<Word>> = MutableStateFlow(listOf(Word()))

    init{
        viewModelScope.launch {
            _words.value = loadWordsEducationalListUseCase()
            currentWordIdx = 0
            _currentWord.value = _words.value[currentWordIdx]
        }
    }

    private var currentWordIdx = 0
    private val _currentWord: MutableStateFlow<Word> = MutableStateFlow(Word())
    val currentWord: StateFlow<Word> =  _currentWord.asStateFlow()

    fun nextWord(){
        currentWordIdx ++
        _currentWord.value = _words.value[currentWordIdx]
    }

    fun saveWord(word: Word){
        viewModelScope.launch {
            saveWordUseCase(word)
        }
    }
}