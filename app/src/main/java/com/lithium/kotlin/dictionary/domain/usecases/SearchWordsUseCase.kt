package com.lithium.kotlin.dictionary.domain.usecases

import com.lithium.kotlin.dictionary.AppScope
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AppScope
class SearchWordsUseCase @Inject constructor(

){
    suspend operator fun invoke(input: String, words: List<Word>): List<Word>{
        val result: Deferred<List<Word>>

        withContext(Dispatchers.IO) {
            result = async {
                words.fold(mutableListOf()) { acc, w ->
                    if (w.sequence.contains(input)) {
                        acc.add(w)
                    }

                    acc
                }
            }
        }

        return result.await()
    }
}