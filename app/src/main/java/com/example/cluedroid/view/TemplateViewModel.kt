package com.example.cluedroid.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cluedroid.repository.TemplateRepository
import com.example.cluedroid.model.Template
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateViewModel @Inject constructor(private val templateRepository: TemplateRepository) :
    ViewModel() {

    fun findTemplateById(id: Int): Template {
        return templateRepository.findTemplateById(id)
    }
    fun getAllTemplates() {
        //TODO
    }
    fun addTemplate(template: Template) = viewModelScope.launch {
        templateRepository.addTemplate(template)
    }

    fun updateTemplate(template: Template) = viewModelScope.launch {
        templateRepository.updateTemplate(template)
    }

}
