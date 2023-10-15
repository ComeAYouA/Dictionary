package com.lithium.kotlin.dictionary.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lithium.kotlin.dictionary.Category
import com.lithium.kotlin.dictionary.Word
import java.util.*

@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    fun getWords(): LiveData<List<Word>>
    @Query("SELECT * FROM word WHERE id=(:id)")
    fun getWord(id: UUID): LiveData<Word?>
    @Query("SELECT * FROM category")
    fun getCategories(): LiveData<List<Category>>
    @Update
    fun updateWord(word: Word)
    @Update
    fun updateCategory(category: Category)
    @Insert
    fun addWord(word: Word)
    @Insert
    fun addCategory(category: Category)
}