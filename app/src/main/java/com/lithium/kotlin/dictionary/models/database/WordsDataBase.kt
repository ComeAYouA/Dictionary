package com.lithium.kotlin.dictionary.models.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lithium.kotlin.dictionary.models.Category
import com.lithium.kotlin.dictionary.models.Word

@Database(entities = [ Word::class, Category::class], version = 1)
@TypeConverters(WordTypeConverters::class)

abstract class WordsDataBase: RoomDatabase() {

    abstract fun wordDao(): WordDao
}