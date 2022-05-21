package com.example.googletranslate.model

data class Translation(
    val `data`: Data
)

data class Data(
    val translations: List<TranslationX>
)

data class TranslationX(
    val translatedText: String
)