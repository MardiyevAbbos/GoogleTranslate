package com.example.googletranslate.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "translate")
data class SaveTranslate(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var textFrom: String,
    var textTo: String
) : Serializable
