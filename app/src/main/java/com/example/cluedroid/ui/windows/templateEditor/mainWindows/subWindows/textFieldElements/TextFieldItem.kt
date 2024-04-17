package com.example.cluedroid.ui.windows.templateEditor.mainWindows.subWindows.textFieldElements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.cluedroid.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
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
    val keyboardController = LocalSoftwareKeyboardController.current

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
                label = { Text(stringResource(R.string.item_s_name, title)) },
                singleLine = true,
                isError = ((text.isEmpty() || text.contains(stringResource(R.string.db_delimiter))) && isTyped),
                supportingText = {
                    if (text.isEmpty() && isTyped) {
                        Text(
                            text = stringResource(R.string.item_s_name_cannot_be_empty, title),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    if (text.contains(stringResource(R.string.db_delimiter)) && isTyped) {
                        Text(
                            text = stringResource(
                                R.string.item_s_name_cannot_contain_delimiter,
                                title
                            ),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                trailingIcon = {
                    if ((text.isEmpty() || text.contains(stringResource(R.string.db_delimiter))) && isTyped) {
                        Icon(Icons.Filled.Info, stringResource(R.string.error), tint = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
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
                    contentDescription = stringResource(R.string.delete_this_item, title)
                )
            }
        }
    }
}
