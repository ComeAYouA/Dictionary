package com.lithium.kotlin.dictionary.features.add_word.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.domain.usecases.AddWordToDictionaryUseCase
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.usecases.TranslateEnteredWordUseCase
import com.lithium.kotlin.dictionary.features.add_word.di.AddWordScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AddWordScope
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