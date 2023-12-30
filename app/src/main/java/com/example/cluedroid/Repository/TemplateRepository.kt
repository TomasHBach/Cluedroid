package com.example.cluedroid.Repository

import androidx.lifecycle.MutableLiveData
import com.example.cluedroid.dao.TemplateDao
import com.example.cluedroid.model.Template
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class TemplateRepository(private val templateDao: TemplateDao) {

    val allTemplates = MutableLiveData<List<Template>>()
    val foundTemplate = MutableLiveData<Template>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun findTemplateById(id: Int): Template? {
        var template: Template? = null
        coroutineScope.launch {
            template = templateDao.findTemplateById(id.toString())
        }
        return template
    }

    fun addTemplate(newTemplate: Template) {
        coroutineScope.launch(Dispatchers.IO) {
            templateDao.addTemplate(newTemplate)
        }
    }

    fun updateTemplate(newTemplate: Template) {
        coroutineScope.launch(Dispatchers.IO) {
            templateDao.updateTemplate(newTemplate)
        }
    }
}