package com.example.cluedroid.ui.windows.cluedroidGame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cluedroid.db.TemplateRoomDatabase
import com.example.cluedroid.repository.ActiveTemplateRepository
import com.example.cluedroid.view.ActiveTemplateViewModel

@Composable
internal fun TabList(
    modifier: Modifier = Modifier,
    title: String,
    items: List<String>,
    tabValues: MutableList<Boolean>,
    dbUpdate: () -> Unit
) {
    ActiveTemplateViewModel(
        ActiveTemplateRepository(
            TemplateRoomDatabase.getInstance(LocalContext.current).activeTemplateDao()
        )
    )

    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clip(RoundedCornerShape(corner = CornerSize(10)))
                .background(MaterialTheme.colorScheme.primaryContainer),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 20.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                text = title,
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(bottom = 15.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                repeat(items.size) {
                    ListItem(itemText = items[it], initialValue = tabValues[it], itemValue = {
                        tabValues[it] = tabValues[it] != true
                        dbUpdate()
                    })
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    TabList(title = "Preview", items = listOf("item1", "item2"), tabValues = mutableListOf(true, false)) {
        
    }
}