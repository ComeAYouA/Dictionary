package com.lithium.kotlin.dictionary.features.edit_word.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.usecases.GetWordUseCase
import com.lithium.kotlin.dictionary.features.edit_word.di.EditWordScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@EditWordScope
class EditWordViewModel @Inject constructor(
    private val getWordUseCase: GetWordUseCase
): ViewModel() {

    private val _word = MutableStateFlow(Word())
    val word = _word.asStateFlow()


    fun loadWord(wordId: UUID) {
        viewModelScope.launch {
            _word.value = getWordUseCase(wordId)?: Word()
        }
    }
}