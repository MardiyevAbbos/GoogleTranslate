package com.example.googletranslate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.googletranslate.model.SaveTranslate

@Database(entities = [SaveTranslate::class], version = 1,exportSchema = true)
abstract class TranslateDatabase : RoomDatabase() {
    abstract fun getTranslateDao(): TranslateDao
}