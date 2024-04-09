package com.example.cluedroid.ui.windows.chooseTemplate

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cluedroid.R
import com.example.cluedroid.db.TemplateRoomDatabase
import com.example.cluedroid.repository.ActiveTemplateRepository
import com.example.cluedroid.repository.TemplateRepository
import com.example.cluedroid.view.ActiveTemplateViewModel
import com.example.cluedroid.view.TemplateViewModel

@Composable
fun TemplateInfo(
    modifier: Modifier = Modifier,
    templateId: Int,
    backFunction: () -> Unit,
    navigateToStartGame: () -> Unit
) {
    //Get View Models
    val templateViewModel = TemplateViewModel(
        TemplateRepository(
            TemplateRoomDatabase.getInstance(LocalContext.current).templateDao()
        )
    )
    //Get list of all templates
    val selectedTemplate = templateViewModel.findTemplateById(templateId)

    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(0.6f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = backFunction) {
                    Icon(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxSize(),
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.go_back_to_template_list_description)
                    )
                }
            }
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                SubList(
                    title = stringResource(R.string.suspects_tab_title),
                    items = selectedTemplate.suspects.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
                )
                SubList(
                    title = stringResource(R.string.weapons_tab_title),
                    items = selectedTemplate.weapons.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
                )
                SubList(
                    title = stringResource(R.string.rooms_tab_title),
                    items = selectedTemplate.rooms.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
                )
            }
        }
        Button(
            modifier = Modifier
                .weight(0.15f)
                .height(100.dp)
                .fillMaxWidth()
                .padding(20.dp),
            onClick = { goToCluedroidGame(templateId, navigateToStartGame, context) },
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Choose",
                fontSize = 35.sp
            )
        }
    }
}


@Composable
private fun SubList(
    modifier: Modifier = Modifier,
    title: String,
    items: List<String>
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            text = title,
            fontSize = 23.sp,
            textAlign = TextAlign.Center
        )
        Divider(color = MaterialTheme.colorScheme.surfaceVariant)
        Spacer(modifier = Modifier.height(5.dp))
        repeat(items.size) {
            SubListItem(
                modifier = Modifier.padding(vertical = 3.dp, horizontal = 20.dp),
                itemText = items[it]
            )
        }
    }
}

@Composable
private fun SubListItem(
    modifier: Modifier = Modifier,
    itemText: String
) {
    val circleColor = MaterialTheme.colorScheme.primaryContainer
    val textColor = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.padding(5.dp))
        Canvas(
            modifier = Modifier
                .size(25.dp)
                .padding(vertical = 5.dp),
            onDraw = {
                drawCircle(color = circleColor)
            }
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = itemText,
            color = textColor,
            fontSize = 20.sp
        )
    }
}

private fun goToCluedroidGame(
    selectedTemplateId: Int,
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
    val template = templateViewModel.findTemplateById(selectedTemplateId)
    //Update the active template index and name
    activeTemplateViewModel.updateSelectedActiveTemplate(selectedTemplateId)
    //Reset the active template table (put everything to true)
    //Get data (to know the size)
    val suspects = template.suspects.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    val weapons = template.weapons.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    val rooms = template.rooms.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    //Resetting active template table
    activeTemplateViewModel.updateSuspectsBooleans(List(suspects.size) {true}.joinToString())
    activeTemplateViewModel.updateWeaponsBooleans(List(weapons.size) {true}.joinToString())
    activeTemplateViewModel.updateRoomsBooleans(List(rooms.size) {true}.joinToString())
    //Go to the updated window
    navigateToStartGame()
}
