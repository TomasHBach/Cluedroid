package com.example.cluedroid.ui.windows.startGame

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cluedroid.R
import com.example.cluedroid.db.TemplateRoomDatabase
import com.example.cluedroid.repository.ActiveTemplateRepository
import com.example.cluedroid.repository.TemplateRepository
import com.example.cluedroid.view.ActiveTemplateViewModel
import com.example.cluedroid.view.TemplateViewModel

@Composable
fun StartGame(
    navigateToCluedroidGame: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    navigateToSelectTemplate: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ) {
        StartGameMain(
            navigateToCluedroidGame,
            navigateToSettings,
            navigateToSelectTemplate
        )
    }
}

@Composable
private fun StartGameMain(
    navigateToCluedroidGame: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    navigateToSelectTemplate: () -> Unit = {}
) {
    //Get View Models
    val templateViewModel = TemplateViewModel(
        TemplateRepository(
            TemplateRoomDatabase.getInstance(LocalContext.current).templateDao()
        )
    )
    val activeTemplateViewModel = ActiveTemplateViewModel(
        ActiveTemplateRepository(
            TemplateRoomDatabase.getInstance(LocalContext.current).activeTemplateDao()
        )
    )
    //Get template and active template
    val templateName = templateViewModel.findTemplateById(
        activeTemplateViewModel.getActiveTemplateData().activeTemplateIndex.toInt()
    ).name

    val iconColor = MaterialTheme.colorScheme.onPrimaryContainer
    val circleColor = MaterialTheme.colorScheme.primaryContainer
    val templateColor = MaterialTheme.colorScheme.primaryContainer

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(navigateToSettings)
        Spacer(modifier = Modifier.height(30.dp))
        Icon(
            imageVector = Icons.Sharp.Search,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .size(170.dp)
                .drawBehind {
                    drawCircle(
                        color = circleColor,
                        radius = (this.size.width / 1.7).toFloat()
                    )
                }
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            modifier = Modifier
                .height(100.dp)
                .width(250.dp),
            onClick = { startGame(context, navigateToCluedroidGame) },
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Start Game",
                fontSize = 35.sp
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Template: ",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = templateName,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .drawBehind {
                        drawRoundRect(
                            color = templateColor,
                            cornerRadius = CornerRadius(30f, 20f)
                        )
                    }
                    .padding(10.dp)
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, top = 20.dp),
            text = stringResource(R.string.another_version_text),
            fontSize = 17.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier
                .height(80.dp)
                .width(230.dp),
            onClick = navigateToSelectTemplate,
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Select Template",
                fontSize = 25.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigateToSettings: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text("")
        },
        actions = {
            IconButton(onClick = navigateToSettings) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = stringResource(R.string.settings_description)
                )
            }
        }
    )
}

private fun startGame(
    context: Context,
    navigateToCluedroidGame: () -> Unit = {}
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
    val template = templateViewModel.findTemplateById(
        activeTemplateViewModel.getActiveTemplateData().activeTemplateIndex.toInt()
    )
    //Reset the active template table (put everything to true)
    //Get data (to know the size)
    val suspects = template.suspects.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    val weapons = template.weapons.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    val rooms = template.rooms.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    //Resetting active template table
    activeTemplateViewModel.updateSuspectsBooleans(List(suspects.size) { true }.joinToString())
    activeTemplateViewModel.updateWeaponsBooleans(List(weapons.size) { true }.joinToString())
    activeTemplateViewModel.updateRoomsBooleans(List(rooms.size) { true }.joinToString())
    //We set the game as started (true)
    activeTemplateViewModel.updateGameStarted(true)

    navigateToCluedroidGame()
}

@Preview
@Composable
private fun Preview() {
}