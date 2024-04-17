package com.example.cluedroid.ui.windows.templateEditor.mainWindows.subWindows

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cluedroid.ui.windows.templateEditor.mainWindows.subWindows.textFieldElements.TextFieldList

@Composable
fun TextFieldWindow(
    modifier: Modifier = Modifier,
    scrollPosition: ScrollState,
    title: String,
    mutableList: MutableList<String>,
    mutableBooleanList: MutableList<MutableState<Boolean>>,
    backButtonFunc: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = backButtonFunc
            ) {
                Icon(
                    modifier = Modifier
                        .padding(3.dp)
                        .fillMaxSize(),
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Go back to previous step"
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 20.dp, top = 5.dp),
                text = "Type the ${title.lowercase()}s' names:",
                fontSize = 21.sp
            )
        }
        TextFieldList(
            modifier = Modifier.weight(0.6f),
            scrollPosition = scrollPosition,
            title = title,
            mutableList = mutableList,
            mutableBooleanList = mutableBooleanList
        )
    }
}
