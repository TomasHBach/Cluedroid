package com.example.cluedroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.cluedroid.ui.theme.CluedroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CluedroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CluedroidMain()
                }
            }
        }
    }
}

@Composable
fun CluedroidMain(modifier: Modifier = Modifier) {
    var numTab by remember {
        mutableStateOf(1)
    }
    val tabTitles = listOf("TAB 1", "TAB 2")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue)
    ) {
        BottomTabs()
    }
}

@Composable
fun BottomTabs() {
    var state by remember { mutableStateOf(0) }
    val titles = listOf("TAB 1", "TAB 2")
    Column {
        TabRow(selectedTabIndex = state, divider = {}) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = state == index,
                    onClick = { state = index }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CluedroidTheme {
        CluedroidMain()
    }
}