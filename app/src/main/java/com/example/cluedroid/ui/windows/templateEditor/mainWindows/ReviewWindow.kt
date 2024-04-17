package com.example.cluedroid.ui.windows.templateEditor.mainWindows

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cluedroid.R

@Composable
fun ReviewWindow(
    modifier: Modifier = Modifier,
    scrollPosition: ScrollState,
    templateName: String,
    suspectsList: List<String>,
    weaponsList: List<String>,
    roomsList: List<String>,
    backFunction: () -> Unit,
    finishButtonFunc: () -> Unit
) {

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
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                text = "Check everything is correct:",
                fontSize = 21.sp,
                textAlign = TextAlign.Center
            )
            Column(
                modifier = Modifier.verticalScroll(scrollPosition)
            ) {
                SubList(
                    title = "Template's Name",
                    items = listOf(templateName)
                )
                SubList(
                    title = stringResource(R.string.suspects_tab_title),
                    items = suspectsList
                )
                SubList(
                    title = stringResource(R.string.weapons_tab_title),
                    items = weaponsList
                )
                SubList(
                    title = stringResource(R.string.rooms_tab_title),
                    items = roomsList
                )
            }
        }
        Button(
            modifier = Modifier
                .weight(0.15f)
                .height(100.dp)
                .fillMaxWidth()
                .padding(20.dp),
            onClick = finishButtonFunc,
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Finish",
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

@Composable
@Preview
private fun Preview() {
    Surface {
        ReviewWindow(scrollPosition = rememberScrollState(),
            templateName = "Test Template",
            suspectsList = listOf("Test1", "Test2", "Test3"),
            weaponsList = listOf("Test1", "Test2", "Test3"),
            roomsList = listOf("Test1", "Test2", "Test3"),
            backFunction = {},
            finishButtonFunc = {}
        )
    }
}
