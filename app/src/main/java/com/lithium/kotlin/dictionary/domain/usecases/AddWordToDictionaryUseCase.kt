package com.lithium.kotlin.dictionary.domain.usecases

import com.lithium.kotlin.dictionary.AppScope
import com.lithium.kotlin.dictionary.domain.models.Category
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AppScope
class AddWordToDictionaryUseCase @Inject constructor(
    private val wordDataBase: WordDao,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(word: Word) {
        withContext(dispatcher){
            wordDataBase.addWord(word)

            var categories = listOf<Category>()

            val getCategoriesJob =  launch(dispatcher) {
                wordDataBase.getCategories().cancellable().collect{
                    categories = it
                    this.coroutineContext.cancel()
                }
            }

            getCategoriesJob.join()

            val categoriesNames = categories.map { it.name }

            updateCategoriesWordsIdsList(word, categories)
            updateCategoriesList(word, categoriesNames)
        }
    }

    private suspend fun updateCategoriesList(word: Word, categoriesNames: List<String>){
        word.categories.forEach { selectedWordCategoryName ->

            if (!categoriesNames.contains(selectedWordCategoryName)){
                wordDataBase.addCategory(
                    Category(
                        selectedWordCategoryName
                    )
                )
            }

        }
    }

    private suspend fun updateCategoriesWordsIdsList(word: Word, categories: List<Category>){
        categories.forEach { category ->

            if(category.ids.contains(word.id)){
                category.ids.add(word.id)

                wordDataBase.updateCategory(
                    category.copy(
                        ids = category.ids
                    )
                )

            }
        }
    }
}
