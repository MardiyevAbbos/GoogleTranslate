package com.example.googletranslate.repository

import com.example.googletranslate.data.local.TranslateDao
import com.example.googletranslate.model.SaveTranslate
import javax.inject.Inject

class HistoryRepository@Inject constructor(
    private val translateDao: TranslateDao) {

    suspend fun getTranslateList() = translateDao.getTranslateList()

    suspend fun removeItem(saveTranslate: SaveTranslate) = translateDao.removeItem(saveTranslate)

}