package com.example.cluedroid.ui.windows.startGame

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cluedroid.R

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
    val iconColor = MaterialTheme.colorScheme.onPrimaryContainer
    val circleColor = MaterialTheme.colorScheme.primaryContainer
    val templateColor = MaterialTheme.colorScheme.primaryContainer

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
            onClick = navigateToCluedroidGame,
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
                text = "Classic",
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

@Preview
@Composable
fun Preview() {
    StartGame()
}