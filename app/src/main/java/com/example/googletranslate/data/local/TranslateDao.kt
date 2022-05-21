package com.example.googletranslate.data.local

import androidx.room.*
import com.example.googletranslate.model.SaveTranslate

@Dao
interface TranslateDao {

    //Translate
    @Query("SELECT * FROM translate")
    suspend fun getTranslateList(): List<SaveTranslate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslate(saveTranslate: SaveTranslate)

    @Query("DELETE FROM translate")
    suspend fun removeAllItem()

    @Delete()
    suspend fun removeItem(saveTranslate: SaveTranslate)

}