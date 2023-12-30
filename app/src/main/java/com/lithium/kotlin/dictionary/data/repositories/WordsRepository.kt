package com.lithium.kotlin.dictionary.data.repositories

import android.content.Context
import androidx.room.Room
import com.lithium.kotlin.dictionary.data.repositories.database.WordsDataBase
import com.lithium.kotlin.dictionary.domain.localdatabasemodels.Category
import com.lithium.kotlin.dictionary.domain.localdatabasemodels.Word
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "words-database"

class WordsRepository private constructor(context: Context) {
    private val dataBase = Room.databaseBuilder(
        context.applicationContext,
        WordsDataBase::class.java,
        DATABASE_NAME
    ).build()

    private val wordDao = dataBase.wordDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun getWords() = wordDao.getWords()
    fun getWord(id: UUID) = wordDao.getWord(id)
    fun getWordsByIds(ids: List<UUID>) = wordDao.getWordsByIds(ids)

    fun getCategories() = wordDao.getCategories()

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

    companion object {
        private var INSTANCE: WordsRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = WordsRepository(context)
            }
        }
        fun get(): WordsRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}