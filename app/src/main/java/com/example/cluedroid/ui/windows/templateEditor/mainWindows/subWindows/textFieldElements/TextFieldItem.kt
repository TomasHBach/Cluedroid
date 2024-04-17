package com.example.cluedroid.ui.windows.templateEditor.mainWindows.subWindows.textFieldElements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
internal fun TextFieldItem(
    modifier: Modifier = Modifier,
    title: String,
    initialText: String,
    visible: Boolean,
    updateText: (String) -> Unit,
    deleteFunc: () -> Unit,
) {
    var text by remember {
        mutableStateOf(initialText)
    }
    var isFirstFocus by remember {
        mutableStateOf(false)
    }
    var isTyped by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Row(
            modifier = modifier
                .padding(start = 15.dp, end = 5.dp, top = 5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            TextField(
                value = initialText,
                onValueChange = {
                    text = it
                    updateText(text)
                },
                label = { Text("$title's name") },
                singleLine = true,
                isError = ((text.isEmpty() || text.contains(";")) && isTyped),
                supportingText = {
                    if (text.isEmpty() && isTyped) {
                        Text(
                            text = "$title's name cannot be empty",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    if (text.contains(";") && isTyped) {
                        Text(
                            text = "$title's name cannot contain ;",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                trailingIcon = {
                    if ((text.isEmpty() || text.contains(";")) && isTyped) {
                        Icon(Icons.Filled.Info, "error", tint = MaterialTheme.colorScheme.error)
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
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        deleteFunc()
                    }
                },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete this $title"
                )
            }
        }
    }
}
