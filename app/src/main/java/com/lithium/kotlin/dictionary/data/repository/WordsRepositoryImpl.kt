package com.lithium.kotlin.dictionary.data.repository

import android.util.Log
import com.lithium.kotlin.dictionary.features.main.di.AppScope
import com.lithium.kotlin.dictionary.domain.models.Category
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import com.lithium.kotlin.dictionary.domain.repository.WordsDataBase
import java.util.UUID
import javax.inject.Inject
import javax.inject.Scope

@Scope
annotation class RepositoryScope
@AppScope
class WordsRepositoryImpl @Inject constructor(
    dataBase: WordsDataBase
) : WordDao{

    private val wordDao = dataBase.wordDao()

    override fun getWords() = wordDao.getWords()
    override suspend fun getWord(id: UUID) = wordDao.getWord(id)
    override fun getWordsByIds(ids: List<UUID>) = wordDao.getWordsByIds(ids)

    override fun getCategories() = wordDao.getCategories()
    override suspend fun getWordsSortedByProgression(): List<Word> = wordDao.getWordsSortedByProgression()

    init {
        Log.d("myTag", "repository init")
    }

    override suspend fun updateWord(word: Word) {
        wordDao.updateWord(word)
    }
    override  suspend fun updateCategory(category: Category) {
        wordDao.updateCategory(category)

    }
    override suspend fun addWord(word: Word) {
        wordDao.addWord(word)

    }
    override suspend fun addCategory(category: Category) {
        wordDao.addCategory(category)

    }
}