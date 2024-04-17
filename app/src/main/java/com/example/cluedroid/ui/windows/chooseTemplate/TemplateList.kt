package com.example.cluedroid.ui.windows.chooseTemplate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cluedroid.R

@Composable
fun TemplateList(
    modifier: Modifier = Modifier,
    templateList: Map<Int, String>,
    changeSelectedTemplate: (Int) -> Unit,
    viewTemplateInfoFunction: () -> Unit,
    navigateToCreateTemplate: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(0.6f)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 20.dp),
                    text = "Select a template",
                    fontSize = 21.sp
                )
            }
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                templateList.forEach { entry ->
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    TemplateListItem(
                        name = entry.value,
                        function = {
                            changeSelectedTemplate(entry.key)
                            viewTemplateInfoFunction()
                        }
                    )
                }
                Divider(color = MaterialTheme.colorScheme.surfaceVariant)
            }
        }
        Button(
            modifier = Modifier
                .weight(0.15f)
                .height(100.dp)
                .fillMaxWidth()
                .padding(20.dp),
            onClick = navigateToCreateTemplate,
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "New Template",
                fontSize = 35.sp
            )
        }
    }
}

@Composable
private fun TemplateListItem(
    modifier: Modifier = Modifier,
    name: String,
    function: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { function() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = name,
            fontSize = 20.sp
        )
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = stringResource(R.string.choose_this_template_desciption)
        )
        Spacer(modifier = Modifier.width(10.dp))
    }
}
