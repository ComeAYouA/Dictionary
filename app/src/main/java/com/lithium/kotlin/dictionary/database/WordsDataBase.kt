package com.lithium.kotlin.dictionary.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lithium.kotlin.dictionary.Category
import com.lithium.kotlin.dictionary.Word

@Database(entities = [ Word::class, Category::class], version = 1)
@TypeConverters(WordTypeConverters::class)

abstract class WordsDataBase: RoomDatabase() {

    abstract fun wordDao(): WordDao
}