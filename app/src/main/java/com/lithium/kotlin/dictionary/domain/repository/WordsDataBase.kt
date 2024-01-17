package com.lithium.kotlin.dictionary.domain.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lithium.kotlin.dictionary.domain.models.Category
import com.lithium.kotlin.dictionary.domain.models.Word

@Database(entities = [ Word::class, Category::class], version = 1)
@TypeConverters(WordTypeConverters::class)

abstract class WordsDataBase: RoomDatabase() {

    abstract fun wordDao(): WordDao
}