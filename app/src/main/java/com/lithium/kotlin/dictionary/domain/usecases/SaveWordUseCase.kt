package com.lithium.kotlin.dictionary.domain.usecases

import com.lithium.kotlin.dictionary.data.repository.RepositoryScope
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import javax.inject.Inject

class SaveWordUseCase @Inject constructor(
    private val repository: WordDao
) {
    suspend operator fun invoke(word: Word){
        repository.updateWord(word)
    }
}