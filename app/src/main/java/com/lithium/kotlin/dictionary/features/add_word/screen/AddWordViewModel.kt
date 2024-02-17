package com.lithium.kotlin.dictionary.features.add_word.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.domain.usecases.AddWordToDictionaryUseCase
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.usecases.GetWordsFlowUseCase
import com.lithium.kotlin.dictionary.domain.usecases.SearchWordsUseCase
import com.lithium.kotlin.dictionary.domain.usecases.TranslateEnteredWordUseCase
import com.lithium.kotlin.dictionary.features.add_word.di.AddWordScope
import com.lithium.kotlin.dictionary.features.dictionary.screen.DictionaryViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AddWordScope
class AddWordViewModel(
    private val addWordToDictionaryUseCase: AddWordToDictionaryUseCase,
    private val translateEnteredWordUseCase: TranslateEnteredWordUseCase
): ViewModel(){

    init {
        Log.d("myTag", "AddWord ViewModel init")
    }

    private val _word: MutableStateFlow<Word> = MutableStateFlow(Word())
    val word: StateFlow<Word> = _word.asStateFlow()

    private val _translation: MutableStateFlow<String> = MutableStateFlow("")
    val translation: StateFlow<String> = _translation.asStateFlow()

    var iconPath = ""

    fun translateRequest(word: String) {
        viewModelScope.launch {
            _translation.value = translateEnteredWordUseCase(word)
        }
    }
    fun addWord(word: Word){
        viewModelScope.launch{
            addWordToDictionaryUseCase(word)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val addWordToDictionaryUseCase: AddWordToDictionaryUseCase,
        private val translateEnteredWordUseCase: TranslateEnteredWordUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == AddWordViewModel::class.java)
            return AddWordViewModel(addWordToDictionaryUseCase, translateEnteredWordUseCase) as T
        }
    }
}

