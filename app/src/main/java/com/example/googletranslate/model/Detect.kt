package com.example.googletranslate.model

data class Detect(
    val `data`: DataR
)

data class DataR(
    val detections: List<List<Detection>>
)

data class Detection(
    val confidence: Double,
    val isReliable: Boolean,
    val language: String
)