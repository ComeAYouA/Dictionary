package com.lithium.kotlin.dictionary.domain.usecases

import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import java.util.UUID
import javax.inject.Inject


class GetWordUseCase @Inject constructor(
    private val repository: WordDao
) {
    suspend operator fun invoke(wordId: UUID) = repository.getWord(wordId)
}