package com.example.googletranslate.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googletranslate.model.SaveTranslate
import com.example.googletranslate.repository.HistoryRepository
import com.example.googletranslate.utils.UiStateList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel@Inject constructor(private val historyRepository: HistoryRepository) : ViewModel() {

    private val _savedListState = MutableStateFlow<UiStateList<SaveTranslate>>(UiStateList.EMPTY)
    val savedListState = _savedListState

    fun getAllTranslate() = viewModelScope.launch {
        _savedListState.value = UiStateList.LOADING
        try {
            val savedListResp = historyRepository.getTranslateList()
            _savedListState.value = UiStateList.SUCCESS(savedListResp)
        }catch (e: Exception){
            _savedListState.value = UiStateList.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    fun removeItem(saveTranslate: SaveTranslate) = viewModelScope.launch {
        historyRepository.removeItem(saveTranslate)
    }

}