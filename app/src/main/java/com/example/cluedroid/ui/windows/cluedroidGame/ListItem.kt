package com.example.cluedroid.ui.windows.cluedroidGame

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun ListItem(
    modifier: Modifier = Modifier,
    itemText: String,
    initialValue: Boolean,
    itemValue: () -> Unit
) {
    val circleColorValueUnmarked = MaterialTheme.colorScheme.primaryContainer
    val circleColorValueMarked = Color.Gray

    val fontColorValueMarked = MaterialTheme.colorScheme.surfaceVariant
    val fontColorValueUnmarked = MaterialTheme.colorScheme.surface

    var textColor by remember {
        mutableStateOf(
            if (initialValue) {
                fontColorValueUnmarked
            } else {
                fontColorValueMarked
            }
        )
    }
    var textDecoration by remember {
        mutableStateOf(
            if (initialValue) {
                TextDecoration.None
            } else {
                TextDecoration.LineThrough
            }
        )
    }
    var circleColor by remember {
        mutableStateOf(
            if (initialValue) {
                circleColorValueUnmarked
            } else {
                circleColorValueMarked
            }
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(bottom = 5.dp, start = 10.dp, end = 10.dp)
            .clip(RoundedCornerShape(20))
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                if (textColor == fontColorValueUnmarked) {
                    textColor = fontColorValueMarked
                    textDecoration = TextDecoration.LineThrough
                    circleColor = circleColorValueMarked
                    itemValue()
                } else {
                    textColor = fontColorValueUnmarked
                    textDecoration = TextDecoration.None
                    circleColor = circleColorValueUnmarked
                    itemValue()
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Spacer(modifier = Modifier.padding(5.dp))
        Canvas(
            modifier = Modifier
                .size(35.dp)
                .padding(vertical = 5.dp),
            onDraw = {
                drawCircle(color = circleColor)
            }
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = itemText,
            color = textColor,
            textDecoration = textDecoration,
            fontSize = 20.sp
        )
    }
}