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
    fun getAllTemplatesIdName(): Map<Int, String> {
        return templateRepository.getAllTemplatesIdName()
    }
    fun addTemplate(template: Template) = viewModelScope.launch {
        templateRepository.addTemplate(template)
    }
    fun getLastIndex(): Int {
        return templateRepository.getLastIndex()
    }
    fun getFirstIndex(): Int {
        return templateRepository.getFirstIndex()
    }
    fun updateTemplate(template: Template) = viewModelScope.launch {
        templateRepository.updateTemplate(template)
    }
    fun deleteTemplate(template: Template) = viewModelScope.launch {
        templateRepository.deleteTemplate(template)
    }

}
