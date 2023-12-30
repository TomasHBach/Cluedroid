package com.example.cluedroid.View

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cluedroid.Repository.TemplateRepository
import com.example.cluedroid.model.Template
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val templateRepository: TemplateRepository) :
    ViewModel() {

    val templateList: LiveData<List<Template>> = templateRepository.allTemplates

    val foundTemplate: LiveData<Template> = templateRepository.foundTemplate

    fun getAllTemplates() {
        templateRepository.allTemplates
    }
    fun addTemplate(template: Template) = viewModelScope.launch {
        templateRepository.addTemplate(template)
    }

    fun updateTemplate(template: Template) = viewModelScope.launch {
        templateRepository.updateTemplate(template)
    }

}

class AppViewModelFactory(private val templateRepository: TemplateRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java))
            return AppViewModel(templateRepository) as T
        throw IllegalArgumentException("Unknown Class for View Model")
    }
}