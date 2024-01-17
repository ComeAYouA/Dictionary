package com.lithium.kotlin.dictionary.data.repository

import android.util.Log
import com.lithium.kotlin.dictionary.AppScope
import com.lithium.kotlin.dictionary.domain.models.Category
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.domain.repository.WordsDataBase
import java.util.UUID
import javax.inject.Inject
import javax.inject.Scope
import javax.inject.Singleton

@Scope
annotation class RepositoryScope
@AppScope
class WordsRepositoryImpl @Inject constructor(dataBase: WordsDataBase) {

    private val wordDao = dataBase.wordDao()

    fun getWords() = wordDao.getWords()
    fun getWord(id: UUID) = wordDao.getWord(id)
    fun getWordsByIds(ids: List<UUID>) = wordDao.getWordsByIds(ids)

    fun getCategories() = wordDao.getCategories()

    init {
        Log.d("myTag", "repository init")
    }

    suspend fun updateWord(word: Word) {
        wordDao.updateWord(word)
    }
    suspend fun updateCategory(category: Category) {
        wordDao.updateCategory(category)

    }
    suspend fun addWord(word: Word) {
        wordDao.addWord(word)

    }
    suspend fun addCategory(category: Category) {
        wordDao.addCategory(category)

    }
}