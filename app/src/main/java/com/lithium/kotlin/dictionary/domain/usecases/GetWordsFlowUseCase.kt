package com.lithium.kotlin.dictionary.domain.usecases

import com.lithium.kotlin.dictionary.data.repository.RepositoryScope
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetWordsFlowUseCase @Inject constructor(
    private val repository: WordDao
){
    operator fun invoke(): Flow<List<Word>> = repository.getWords().flowOn(Dispatchers.IO)
}