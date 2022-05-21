package com.example.googletranslate.di

import android.content.Context
import androidx.room.Room
import com.example.googletranslate.data.local.TranslateDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun getDatabase(@ApplicationContext context: Context): TranslateDatabase=
        Room.databaseBuilder(context,TranslateDatabase::class.java,"translate.db").build()

    @Provides
    @Singleton
    fun getBasketDao(database: TranslateDatabase) = database.getTranslateDao()

}