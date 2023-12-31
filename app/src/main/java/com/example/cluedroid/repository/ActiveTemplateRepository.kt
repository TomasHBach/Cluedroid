package com.example.cluedroid.repository

import com.example.cluedroid.dao.ActiveTemplateDao
import com.example.cluedroid.model.ActiveTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActiveTemplateRepository(private val activeTemplateDao: ActiveTemplateDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun getActiveTemplateData(): ActiveTemplate {
        return activeTemplateDao.getActiveTemplateData()
    }

    fun updateSuspectsBooleans(data: String) {
        coroutineScope.launch(Dispatchers.IO) {
            activeTemplateDao.updateSuspectsBooleans(data)
        }
    }

    fun updateWeaponsBooleans(data: String) {
        coroutineScope.launch(Dispatchers.IO) {
            activeTemplateDao.updateWeaponsBooleans(data)
        }
    }

    fun updateRoomsBooleans(data: String) {
        coroutineScope.launch(Dispatchers.IO) {
            activeTemplateDao.updateRoomsBooleans(data)
        }
    }

    fun updateActiveTemplate(activeTemplate: ActiveTemplate) {
        coroutineScope.launch(Dispatchers.IO) {
            activeTemplateDao.updateActiveTemplate(activeTemplate)
        }
    }

}