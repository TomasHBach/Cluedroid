package com.example.cluedroid.ui.windows.templateEditor.mainWindows.subWindows

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cluedroid.R

@Composable
fun TemplateNameWindow(
    templateName: String,
    updateTemplateName: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ) {
        TemplateNameWindowMain(
            templateName = templateName,
            updateTemplateName = updateTemplateName
        )
    }
}

@Composable
private fun TemplateNameWindowMain(
    modifier: Modifier = Modifier,
    templateName: String,
    updateTemplateName: (String) -> Unit
) {
    var isFirstFocus by remember {
        mutableStateOf(false)
    }
    var isTyped by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 20.dp),
                text = stringResource(R.string.type_the_template_s_name),
                fontSize = 21.sp
            )
        }
        Column(
            modifier = Modifier.weight(0.6f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier
                    .padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {
                TextField(
                    value = templateName,
                    onValueChange = { updateTemplateName(it) },
                    label = { Text(stringResource(R.string.template_s_name_description)) },
                    singleLine = true,
                    isError = (templateName.isEmpty() && isTyped),
                    supportingText = {
                        if (templateName.isEmpty() && isTyped) {
                            Text(
                                text = stringResource(R.string.template_s_name_cannot_be_empty),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    trailingIcon = {
                        if (templateName.isEmpty() && isTyped) {
                            Icon(Icons.Filled.Info,
                                stringResource(R.string.error), tint = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier
                        .weight(0.8f)
                        .onFocusChanged {
                            if (it.isFocused) {
                                isFirstFocus = true
                            }
                            if (!it.isFocused && isFirstFocus) {
                                isTyped = true
                            }
                        }
                )
            }
        }
    }
}
