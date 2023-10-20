package com.lithium.kotlin.dictionary.models

import android.content.Context
import androidx.room.Room
import com.lithium.kotlin.dictionary.models.database.WordsDataBase
import com.lithium.kotlin.dictionary.models.Category
import com.lithium.kotlin.dictionary.models.Word
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

    fun updateWord(word: Word) {
        executor.execute {
            wordDao.updateWord(word)
        }
    }
    fun updateCategory(category: Category) {
        executor.execute {
            wordDao.updateCategory(category)
        }
    }
    fun addWord(word: Word) {
        executor.execute {
            wordDao.addWord(word)
        }
    }
    fun addCategory(category: Category) {
        executor.execute{
            wordDao.addCategory(category)
        }
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