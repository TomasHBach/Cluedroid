package com.example.cluedroid.ui.windows.templateEditor.mainWindows.subWindows.textFieldElements

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cluedroid.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TextFieldList(
    modifier: Modifier = Modifier,
    scrollPosition: ScrollState,
    title: String,
    mutableList: MutableList<String>,
    mutableBooleanList: MutableList<MutableState<Boolean>>
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier.verticalScroll(scrollPosition),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (i in 0 until mutableList.size) {
            TextFieldItem(
                title = title,
                initialText = mutableList[i],
                visible = mutableBooleanList[i].value,
                updateText = { mutableList[i] = it },
                deleteFunc = {
                    mutableBooleanList[i].value = false
                }
            )
        }
        Button(
            modifier = Modifier.size(230.dp, 70.dp),
            onClick = {
                coroutineScope.launch {
                    mutableList.add("")
                    mutableBooleanList.add(mutableStateOf(false))
                    delay(50)
                    mutableBooleanList.last().value = true
                }
            },
            shape = RoundedCornerShape(20.dp)
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(R.string.add_a_item, title)
            )
        }
        /*for (i in 0 until mutableList.size) {
            Text(text = mutableList[i] + " : " + mutableBooleanList[i].value)
        }*/
    }
}
