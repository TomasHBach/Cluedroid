package com.example.cluedroid.repository

import com.example.cluedroid.dao.TemplateDao
import com.example.cluedroid.model.Template
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TemplateRepository(private val templateDao: TemplateDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun findTemplateById(id: Int): Template {
        return templateDao.findTemplateById(id.toString())
    }

    fun addTemplate(newTemplate: Template) {
        coroutineScope.launch(Dispatchers.IO) {
            templateDao.addTemplate(newTemplate)
        }
    }

    fun updateTemplate(template: Template) {
        coroutineScope.launch(Dispatchers.IO) {
            templateDao.updateTemplate(template)
        }
    }
}