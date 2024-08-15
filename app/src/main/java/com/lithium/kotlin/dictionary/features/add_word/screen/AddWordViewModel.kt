package com.lithium.kotlin.dictionary.features.add_word.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.domain.models.Category
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
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@AddWordScope
class AddWordViewModel(
    private val addWordToDictionaryUseCase: AddWordToDictionaryUseCase,
    private val translateEnteredWordUseCase: TranslateEnteredWordUseCase
): ViewModel(){

    private val _word: MutableStateFlow<String> = MutableStateFlow("")
    val word = _word.asStateFlow()

    private val _translation= MutableStateFlow(mutableSetOf<String>())
    val translation = _translation.asStateFlow()

    private val _categories= MutableStateFlow(mutableSetOf<String>())
    val categories = _categories.asStateFlow()

    private val _iconPath: MutableStateFlow<String> = MutableStateFlow("")
    val iconPath = _iconPath.asStateFlow()

    fun addWord(){
        val word = Word(
            sequence = word.value,
            translation = translation.value,
            categories = categories.value,
            photoFilePath = iconPath.value
        )
        viewModelScope.launch{
            addWordToDictionaryUseCase(word)
        }
    }

    fun updateIconPath(path: String) = _iconPath.update { path }
    fun updateWord(word: String) {
        viewModelScope.launch {
            _word.update { word }
            val translation = translateEnteredWordUseCase(word)
            _translation.update { translation.toMutableSet() }
        }
    }
    fun addCategory(category: String){
        val new = mutableSetOf<String>()
        new.addAll(categories.value)
        new.add(category)
        _categories.value = new
    }

    fun removeCategory(category: String){
        val new = mutableSetOf<String>()
        new.addAll(categories.value)
        new.remove(category)
        _categories.value = new
    }

    fun addTranslation(word: String){
        val new = mutableSetOf<String>()
        new.addAll(translation.value)
        new.add(word)
        _translation.value = new
    }

    fun removeTranslation(word: String){
        val new = mutableSetOf<String>()
        new.addAll(translation.value)
        new.remove(word)
        _translation.value = new
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

