package com.lithium.kotlin.dictionary.data.di

import android.content.Context
import androidx.room.Room
import com.lithium.kotlin.dictionary.AppScope
import com.lithium.kotlin.dictionary.data.repository.WordsRepositoryImpl
import com.lithium.kotlin.dictionary.domain.repository.WordDao
import com.lithium.kotlin.dictionary.domain.repository.WordsDataBase
import com.lithium.kotlin.dictionary.domain.repository.WordsDataBase_Impl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Scope

private const val DATABASE_NAME = "words-database"
@Module
interface RepositoryModule {

    @Binds
    fun bindsWordsDataBaseImpl_to_WordDao(impl: WordsRepositoryImpl): WordDao

    companion object {
        @Provides
        fun provideWordsDataBase(context: Context): WordsDataBase{
            return Room.databaseBuilder(
                context,
                WordsDataBase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}