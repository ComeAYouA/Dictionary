package com.lithium.kotlin.dictionary.features.edit_word.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.usecases.GetWordUseCase
import com.lithium.kotlin.dictionary.domain.usecases.SaveWordUseCase
import com.lithium.kotlin.dictionary.features.edit_word.di.EditWordScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@EditWordScope
class EditWordViewModel @Inject constructor(
    private val getWordUseCase: GetWordUseCase,
    private val saveWordUseCase: SaveWordUseCase
): ViewModel() {

    private val _word = MutableStateFlow(Word())
    val word = _word.asStateFlow()
    fun loadWord(wordId: UUID) {
        viewModelScope.launch {
            _word.value = getWordUseCase(wordId)?: Word()
        }
    }

    fun saveWord(word: Word){
        viewModelScope.launch {
            saveWordUseCase(word)
            Log.d("myWord", "word save")
        }
    }

//    @Suppress("UNCHECKED_CAST")
//    class Factory @Inject constructor(
//        private val getWordUseCase: GetWordUseCase,
//        private val saveWordUseCase: SaveWordUseCase
//    ): ViewModelProvider.Factory{
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            require(modelClass == EditWordViewModel::class.java)
//            return EditWordViewModel(getWordUseCase, saveWordUseCase) as T
//        }
//    }
}