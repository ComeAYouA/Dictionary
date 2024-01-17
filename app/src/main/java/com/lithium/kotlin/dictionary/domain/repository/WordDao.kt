package com.lithium.kotlin.dictionary.domain.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lithium.kotlin.dictionary.domain.models.Category
import com.lithium.kotlin.dictionary.domain.models.Word
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    fun getWords(): Flow<List<Word>>
    @Query("SELECT * FROM word WHERE id=(:id)")
    fun getWord(id: UUID): Flow<Word?>
    @Query("SELECT * FROM word WHERE id IN (:ids)")
    fun getWordsByIds(ids: List<UUID>): Flow<List<Word>>
    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<Category>>
    @Update
    suspend fun updateWord(word: Word)
    @Update
    suspend fun updateCategory(category: Category)
    @Insert
    suspend fun addWord(word: Word)
    @Insert
    suspend fun addCategory(category: Category)
}