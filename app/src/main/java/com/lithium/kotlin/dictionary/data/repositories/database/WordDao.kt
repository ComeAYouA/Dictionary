package com.lithium.kotlin.dictionary.data.repositories.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lithium.kotlin.dictionary.domain.localdatabasemodels.Category
import com.lithium.kotlin.dictionary.domain.localdatabasemodels.Word
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