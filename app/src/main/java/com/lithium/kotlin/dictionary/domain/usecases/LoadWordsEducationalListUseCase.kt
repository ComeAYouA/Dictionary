package com.lithium.kotlin.dictionary.domain.usecases

import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import javax.inject.Inject

class LoadWordsEducationalListUseCase @Inject constructor(
    private val repository: WordDao
){
    suspend operator fun invoke(): List<Word>{
        return repository.getWordsSortedByProgression()
    }
}