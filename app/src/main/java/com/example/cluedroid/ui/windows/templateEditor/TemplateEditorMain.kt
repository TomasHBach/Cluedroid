package com.example.cluedroid.ui.windows.templateEditor

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.platform.LocalContext
import com.example.cluedroid.db.TemplateRoomDatabase
import com.example.cluedroid.model.Template
import com.example.cluedroid.repository.ActiveTemplateRepository
import com.example.cluedroid.repository.TemplateRepository
import com.example.cluedroid.view.ActiveTemplateViewModel
import com.example.cluedroid.view.TemplateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TemplateEditorEditMode(
    editModeTemplateId: Int,
    navigateBack: () -> Unit,
    navigateToStartGame: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    //Get View Models
    val templateViewModel = TemplateViewModel(
        TemplateRepository(
            TemplateRoomDatabase.getInstance(LocalContext.current).templateDao()
        )
    )
    //Get template
    val template = templateViewModel.findTemplateById(editModeTemplateId)
    //Set the editor values
    var templateName by remember {
        mutableStateOf(template.name)
    }
    val suspectsList =
        template.suspects.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    val weaponsList =
        template.weapons.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    val roomsList = template.rooms.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    //Suspects lists
    val suspectsMutableList = remember {
        suspectsList.toMutableStateList()
    }
    val suspectsMutableBooleanList = remember {
        MutableList(suspectsMutableList.size) { mutableStateOf(true) }
    }
    //Weapons lists
    val weaponsMutableList = remember {
        weaponsList.toMutableStateList()
    }
    val weaponsMutableBooleanList = remember {
        MutableList(weaponsMutableList.size) { mutableStateOf(true) }
    }
    //Rooms lists
    val roomsMutableList = remember {
        roomsList.toMutableStateList()
    }
    val roomsMutableBooleanList = remember {
        MutableList(roomsMutableList.size) { mutableStateOf(true) }
    }

    //Building the editor
    TemplateEditor(
        navigateBack = navigateBack,
        templateName = templateName,
        updateTemplateName = { templateName = it },
        suspectsMutableList = suspectsMutableList,
        suspectsMutableBooleanList = suspectsMutableBooleanList,
        weaponsMutableList = weaponsMutableList,
        weaponsMutableBooleanList = weaponsMutableBooleanList,
        roomsMutableList = roomsMutableList,
        roomsMutableBooleanList = roomsMutableBooleanList
    ) {
        updateTemplate(
            navigateToStartGame,
            context,
            coroutineScope,
            editModeTemplateId,
            templateName,
            suspectsMutableList,
            weaponsMutableList,
            roomsMutableList
        )
    }
}

@Composable
fun TemplateEditorCreateMode(
    navigateBack: () -> Unit,
    navigateToStartGame: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var templateName by remember {
        mutableStateOf("")
    }
    val suspectsList = listOf("")
    val weaponsList = listOf("")
    val roomsList = listOf("")
    //Suspects lists
    val suspectsMutableList = remember {
        suspectsList.toMutableStateList()
    }
    val suspectsMutableBooleanList = remember {
        MutableList(suspectsMutableList.size) { mutableStateOf(true) }
    }
    //Weapons lists
    val weaponsMutableList = remember {
        weaponsList.toMutableStateList()
    }
    val weaponsMutableBooleanList = remember {
        MutableList(weaponsMutableList.size) { mutableStateOf(true) }
    }
    //Rooms lists
    val roomsMutableList = remember {
        roomsList.toMutableStateList()
    }
    val roomsMutableBooleanList = remember {
        MutableList(roomsMutableList.size) { mutableStateOf(true) }
    }

    //Building the editor
    TemplateEditor(
        navigateBack = navigateBack,
        templateName = templateName,
        updateTemplateName = { templateName = it },
        suspectsMutableList = suspectsMutableList,
        suspectsMutableBooleanList = suspectsMutableBooleanList,
        weaponsMutableList = weaponsMutableList,
        weaponsMutableBooleanList = weaponsMutableBooleanList,
        roomsMutableList = roomsMutableList,
        roomsMutableBooleanList = roomsMutableBooleanList
    ) {
        createTemplate(
            navigateToStartGame,
            context,
            coroutineScope,
            templateName,
            suspectsMutableList,
            weaponsMutableList,
            roomsMutableList
        )
    }
}

private fun createTemplate(
    navigateToStartGame: () -> Unit,
    context: Context,
    coroutineScope: CoroutineScope,
    templateName: String,
    suspectsMutableList: MutableList<String>,
    weaponsMutableList: MutableList<String>,
    roomsMutableList: MutableList<String>,
) {
    //Get View Models
    val templateViewModel = TemplateViewModel(
        TemplateRepository(
            TemplateRoomDatabase.getInstance(context).templateDao()
        )
    )
    coroutineScope.launch {
        //Add template
        templateViewModel.addTemplate(
            Template(
                name = templateName,
                suspects = suspectsMutableList.joinToString(separator = ";"),
                weapons = weaponsMutableList.joinToString(separator = ";"),
                rooms = roomsMutableList.joinToString(separator = ";")
            )
        )
        delay(200)
        //Set as selected template
        goToStartGame(
            navigateToStartGame = navigateToStartGame,
            context = context
        )
    }
}

private fun updateTemplate(
    navigateToStartGame: () -> Unit,
    context: Context,
    coroutineScope: CoroutineScope,
    templateId: Int,
    templateName: String,
    suspectsMutableList: MutableList<String>,
    weaponsMutableList: MutableList<String>,
    roomsMutableList: MutableList<String>,
) {
    //Get View Models
    val templateViewModel = TemplateViewModel(
        TemplateRepository(
            TemplateRoomDatabase.getInstance(context).templateDao()
        )
    )
    coroutineScope.launch {
        //Add template
        templateViewModel.updateTemplate(
            Template(
                id = templateId,
                name = templateName,
                suspects = suspectsMutableList.joinToString(separator = ";"),
                weapons = weaponsMutableList.joinToString(separator = ";"),
                rooms = roomsMutableList.joinToString(separator = ";")
            )
        )
        delay(200)
        //Set as selected template
        goToStartGame(
            setTemplateIdName = templateId,
            navigateToStartGame = navigateToStartGame,
            context = context
        )
    }
}

private fun goToStartGame(
    setTemplateIdName: Int? = null,
    navigateToStartGame: () -> Unit,
    context: Context
) {
    //Get View Models
    val templateViewModel = TemplateViewModel(
        TemplateRepository(
            TemplateRoomDatabase.getInstance(context).templateDao()
        )
    )
    val activeTemplateViewModel = ActiveTemplateViewModel(
        ActiveTemplateRepository(
            TemplateRoomDatabase.getInstance(context).activeTemplateDao()
        )
    )
    //Get active template
    val template = if (setTemplateIdName == null)
        templateViewModel.findTemplateById(templateViewModel.getLastIndex())
    else
        templateViewModel.findTemplateById(setTemplateIdName)
    //Update the active template index and name
    activeTemplateViewModel.updateSelectedActiveTemplate(template.id)
    //Reset the active template table (put everything to true)
    //Get data (to know the size)
    val suspects = template.suspects.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    val weapons = template.weapons.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    val rooms = template.rooms.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    //Resetting active template table
    activeTemplateViewModel.updateSuspectsBooleans(List(suspects.size) { true }.joinToString())
    activeTemplateViewModel.updateWeaponsBooleans(List(weapons.size) { true }.joinToString())
    activeTemplateViewModel.updateRoomsBooleans(List(rooms.size) { true }.joinToString())
    //Go to the updated window
    navigateToStartGame()
}
