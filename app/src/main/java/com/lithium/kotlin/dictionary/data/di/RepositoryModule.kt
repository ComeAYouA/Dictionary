package com.lithium.kotlin.dictionary.data.di

import android.content.Context
import androidx.room.Room
import com.lithium.kotlin.dictionary.AppScope
import com.lithium.kotlin.dictionary.domain.repository.WordsDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Scope

private const val DATABASE_NAME = "words-database"
@Module
class RepositoryModule {
    @Provides
    fun provideWordsDataBase(context: Context): WordsDataBase{
        return Room.databaseBuilder(
            context,
            WordsDataBase::class.java,
            DATABASE_NAME
        ).build()
    }
}