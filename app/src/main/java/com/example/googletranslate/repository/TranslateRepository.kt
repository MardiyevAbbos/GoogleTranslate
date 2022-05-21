package com.example.googletranslate.repository

import com.example.googletranslate.data.local.TranslateDao
import com.example.googletranslate.data.remote.ApiService
import com.example.googletranslate.model.SaveTranslate
import javax.inject.Inject

class TranslateRepository @Inject constructor(
    private val apiService: ApiService,
    private val translateDao: TranslateDao) {

    suspend fun wordTranslate(word: String,target: String, source:String) = apiService.wordTranslate(word, target, source)

    suspend fun wordDetect(word: String) = apiService.wordDetect(word)

    suspend fun insertTranslate(saveTranslate: SaveTranslate) = translateDao.insertTranslate(saveTranslate)

}