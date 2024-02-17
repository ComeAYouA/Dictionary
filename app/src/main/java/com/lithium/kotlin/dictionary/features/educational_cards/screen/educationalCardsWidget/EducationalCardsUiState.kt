package com.lithium.kotlin.dictionary.features.educational_cards.screen.educationalCardsWidget

import com.lithium.kotlin.dictionary.domain.models.Word
import java.util.Stack

sealed interface EducationalCardsUiState {
    data object Loading: EducationalCardsUiState

    data class Success(
        val educationalCards: Stack<EducationalCardUiState>
    ): EducationalCardsUiState
}

data class EducationalCardUiState(
    val word: Word
)
