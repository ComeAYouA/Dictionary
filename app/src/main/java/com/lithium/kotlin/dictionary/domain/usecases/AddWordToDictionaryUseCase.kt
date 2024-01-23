package com.lithium.kotlin.dictionary.domain.usecases

import com.lithium.kotlin.dictionary.AppScope
import com.lithium.kotlin.dictionary.domain.models.Category
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AppScope
class AddWordToDictionaryUseCase @Inject constructor(
    private val wordDataBase: WordDao,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(word: Word) {

        withContext(dispatcher) {
            wordDataBase.getCategories().collect { categories ->

                val categoriesNames = categories.map { it.name }

                wordDataBase.addWord(word)

                word.categories.forEach {
                    if (!categoriesNames.contains(it)) {
                        wordDataBase.addCategory(Category(it, mutableSetOf(word.id)))
                    }
                }
                categories.forEach{
                    if (word.categories.contains(it.name)){
                        if (!it.ids.contains(word.id)){
                            it.ids.add(word.id)
                            wordDataBase.updateCategory(it)
                        }
                    }
                }
            }
        }
    }
}