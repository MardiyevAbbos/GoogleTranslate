package com.example.googletranslate.ui.translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googletranslate.model.Detect
import com.example.googletranslate.model.SaveTranslate
import com.example.googletranslate.model.Translation
import com.example.googletranslate.repository.TranslateRepository
import com.example.googletranslate.utils.UiStateList
import com.example.googletranslate.utils.UiStateObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(private val translateRepository: TranslateRepository) : ViewModel() {

    private val _translateState = MutableStateFlow<UiStateObject<Translation>>(UiStateObject.EMPTY)
    val translateState = _translateState

    fun wordTranslate(word: String,target: String, source: String) = viewModelScope.launch {
        _translateState.value = UiStateObject.LOADING
        try {
            val translateResp = translateRepository.wordTranslate(word, target, source)
            _translateState.value =UiStateObject.SUCCESS(translateResp)
        }catch (e: Exception){
            _translateState.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _detectState = MutableStateFlow<UiStateObject<Detect>>(UiStateObject.EMPTY)
    val detectState = _detectState

    fun wordDetect(word: String) = viewModelScope.launch {
        _detectState.value = UiStateObject.LOADING
        try {
            val detectResp = translateRepository.wordDetect(word)
            _detectState.value =UiStateObject.SUCCESS(detectResp)
        }catch (e: Exception){
            _detectState.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }


    fun insertTranslate(saveTranslate: SaveTranslate) = viewModelScope.launch {
        translateRepository.insertTranslate(saveTranslate)
    }

}