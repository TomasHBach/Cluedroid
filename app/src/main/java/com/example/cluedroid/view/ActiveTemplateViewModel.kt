package com.example.cluedroid.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cluedroid.model.ActiveTemplate
import com.example.cluedroid.repository.ActiveTemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActiveTemplateViewModel @Inject constructor(private val activeTemplateRepository: ActiveTemplateRepository) :
    ViewModel() {

    fun getActiveTemplateData(): ActiveTemplate {
        return activeTemplateRepository.getActiveTemplateData()
    }

    fun updateSuspectsBooleans(data: String) = viewModelScope.launch {
        activeTemplateRepository.updateSuspectsBooleans(data)
    }

    fun updateWeaponsBooleans(data: String) = viewModelScope.launch {
        activeTemplateRepository.updateWeaponsBooleans(data)
    }

    fun updateRoomsBooleans(data: String) = viewModelScope.launch {
        activeTemplateRepository.updateRoomsBooleans(data)
    }

    fun updateActiveTemplate(activeTemplate: ActiveTemplate) = viewModelScope.launch {
        activeTemplateRepository.updateActiveTemplate(activeTemplate)
    }
}