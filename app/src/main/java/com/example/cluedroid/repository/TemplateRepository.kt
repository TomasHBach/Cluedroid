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

    fun getAllTemplatesIdName(): Map<Int, String> {
        return templateDao.getAllTemplatesIdName().associateBy ({ it.id }, {it.name})
    }

    fun getLastIndex(): Int {
        return templateDao.getLastIndex()
    }

    fun getFirstIndex(): Int {
        return templateDao.getFirstIndex()
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

    fun deleteTemplate(template: Template) {
        coroutineScope.launch {
            templateDao.deleteTemplate(template)
        }
    }
}