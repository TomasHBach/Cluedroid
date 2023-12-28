package com.example.cluedroid.View

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cluedroid.Repository.TemplateRepository
import com.example.cluedroid.model.Template
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val templateRepository: TemplateRepository) :
    ViewModel() {

    val templateList: LiveData<List<Template>> = templateRepository.allTemplates

    val foundTemplate: LiveData<Template> = templateRepository.foundTemplate

    fun getAllTemplates() {
        templateRepository.allTemplates
    }
    fun addTemplate(template: Template) {
        templateRepository.addTemplate(template)
        getAllTemplates()
    }

    fun updateTemplate(template: Template) {
        templateRepository.updateTemplate(template)
        getAllTemplates()
    }

}