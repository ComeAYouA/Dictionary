package com.lithium.kotlin.dictionary.features.educational_cards.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.usecases.LoadWordsEducationalListUseCase
import com.lithium.kotlin.dictionary.domain.usecases.SaveWordUseCase
import com.lithium.kotlin.dictionary.features.educational_cards.screen.educationalCardsWidget.EducationalCard
import com.lithium.kotlin.dictionary.features.educational_cards.screen.educationalCardsWidget.EducationalCardUiState
import com.lithium.kotlin.dictionary.features.educational_cards.screen.educationalCardsWidget.EducationalCardsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Stack
import javax.inject.Inject

class EducationalCardsViewModel(
    private val loadWordsEducationalListUseCase: LoadWordsEducationalListUseCase,
    private val saveWordUseCase: SaveWordUseCase
): ViewModel() {

    val educationalCardUiState: MutableStateFlow<EducationalCardsUiState>
        = MutableStateFlow(EducationalCardsUiState.Loading)


    init{
        viewModelScope.launch {
            educationalCardUiState.value = EducationalCardsUiState.Success(

                loadWordsEducationalListUseCase().fold(Stack()){ acc, word ->
                    acc.push(
                        EducationalCardUiState(
                            word
                        )
                    )

                    acc
                }

            )
        }

    }

    fun nextWord(){
        (educationalCardUiState.value as EducationalCardsUiState.Success).educationalCards.pop()
    }

    fun saveWord(word: Word){
        viewModelScope.launch {
            saveWordUseCase(word)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val loadWordsEducationalListUseCase: LoadWordsEducationalListUseCase,
        private val saveWordUseCase: SaveWordUseCase
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == EducationalCardsViewModel::class.java)
            return EducationalCardsViewModel(loadWordsEducationalListUseCase, saveWordUseCase) as T
        }
    }
}