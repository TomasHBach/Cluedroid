package com.example.cluedroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cluedroid.ui.theme.CluedroidTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CluedroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    CluedroidMain()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CluedroidMain(modifier: Modifier = Modifier) {
    val tabTitles = listOf("Hide", "Suspects")
    val tabIconsSelected = listOf(Icons.Filled.Home, Icons.Filled.Face)
    val tabIconsNotSelected = listOf(Icons.Outlined.Home, Icons.Outlined.Face)
    val tabs = listOf(HideTab(), SuspectsTab())

    val pagerState = rememberPagerState( pageCount = { tabs.size } )
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue),

        ) {
        TopBar()

        HorizontalPager(
            modifier = Modifier
                .weight(0.9f)
                .fillMaxWidth(),
            state = pagerState,
            beyondBoundsPageCount = 1
        ) { page ->
            when (page) {
                0 -> HideTab(
                    Modifier
                        .weight(0.9f)
                        .fillMaxSize()
                )

                1 -> SuspectsTab(
                    Modifier
                        .weight(0.9f)
                        .fillMaxSize()
                )
            }
        }

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.size(0.dp),
                    color = Color.Transparent,
                )
            },
            modifier = Modifier
                .weight(0.1f)
                .fillMaxWidth()
        ) {
            repeat(tabTitles.size) {
                Tab(text = { Text(tabTitles[it]) },
                    icon = {
                        Icon(
                            if (pagerState.currentPage == it) tabIconsSelected[it] else tabIconsNotSelected[it],
                            contentDescription = null
                        )
                    },
                    selected = pagerState.currentPage == it,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(it) }})
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.LightGray
        ),
        title = {
            Text("")
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Rounded.Refresh, contentDescription = "New Game")
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Rounded.Settings, contentDescription = "Settings")
            }
        }
    )
}

@Composable
fun HideTab(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Rounded.Search, contentDescription = null, modifier = Modifier.size(200.dp)
        )
        Text(
            text = "Use this page to hide your notes from peaking eyes 👀",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(350.dp)
        )
    }
}

@Composable
fun SuspectsTab(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        repeat(10) {
            Text(text = "Elemento $it")
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