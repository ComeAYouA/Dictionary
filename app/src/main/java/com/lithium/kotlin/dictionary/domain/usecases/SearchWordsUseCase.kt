package com.lithium.kotlin.dictionary.domain.usecases

import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.AppScope
import com.lithium.kotlin.dictionary.data.repository.RepositoryScope
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import com.lithium.kotlin.dictionary.presentation.dictionary.DictionaryFragmentScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AppScope
class SearchWordsUseCase @Inject constructor(
    private val repository: WordDao
){
    suspend operator fun invoke(input: String, words: List<Word>): List<Word>{
        val result: Deferred<List<Word>>

        withContext(Dispatchers.IO) {
            result = async {
                words.fold(mutableListOf<Word>()) { acc, w ->
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