package com.example.cluedroid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cluedroid.db.TemplateRoomDatabase
import com.example.cluedroid.repository.ActiveTemplateRepository
import com.example.cluedroid.repository.TemplateRepository

import com.example.cluedroid.ui.theme.CluedroidTheme
import com.example.cluedroid.view.ActiveTemplateViewModel
import com.example.cluedroid.view.TemplateViewModel
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
fun CluedroidMain(
    modifier: Modifier = Modifier
) {
    val templateViewModel = TemplateViewModel(
        TemplateRepository(
            TemplateRoomDatabase.getInstance(LocalContext.current).templateDao()
        )
    )
    val activeTemplateViewModel = ActiveTemplateViewModel(
        ActiveTemplateRepository(
            TemplateRoomDatabase.getInstance(LocalContext.current).activeTemplateDao()
        )
    )
    val template = templateViewModel.findTemplateById(
        activeTemplateViewModel.getActiveTemplateData().activeTemplateIndex.toInt()
    )
    val activeTemplate = activeTemplateViewModel.getActiveTemplateData()
    val suspects = template.suspects.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    val weapons = template.weapons.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    val rooms = template.rooms.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()

    val suspectsValues =
        activeTemplate.suspectsBooleans.trim().splitToSequence(", ").filter { it.isNotEmpty() }
            .toList().map { it.toBoolean() }
    val weaponsValues =
        activeTemplate.weaponsBooleans.trim().splitToSequence(", ").filter { it.isNotEmpty() }
            .toList().map { it.toBoolean() }
    val roomsValues =
        activeTemplate.roomsBooleans.trim().splitToSequence(", ").filter { it.isNotEmpty() }
            .toList().map { it.toBoolean() }
    /*val suspects = null
    val weapons = null
    val rooms = null
    val test = listOf(true,true,false).joinToString()
    val str = test.trim().splitToSequence(", ").filter { it.isNotEmpty() }.toList()
    */

    val tabTitles = listOf("Hide", "Suspects", "Weapons", "Rooms")
    val tabIconsSelected = listOf(
        painterResource(id = R.drawable.baseline_home_24),
        painterResource(id = R.drawable.baseline_person_search_24),
        painterResource(id = R.drawable.baseline_hardware_24),
        painterResource(id = R.drawable.baseline_meeting_room_24)
    )
    val tabIconsNotSelected = listOf(
        painterResource(id = R.drawable.outline_home_24),
        painterResource(id = R.drawable.outline_person_search_24),
        painterResource(id = R.drawable.outline_hardware_24),
        painterResource(id = R.drawable.outline_meeting_room_24)
    )

    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
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
            beyondBoundsPageCount = 4
        ) { page ->
            when (page) {
                0 -> HideTab(
                    Modifier
                        .fillMaxSize()
                )

                1 -> SuspectsTab(
                    Modifier
                        .fillMaxSize(),
                    suspectList = suspects,
                    suspectsValues = suspectsValues
                )

                2 -> WeaponsTab(
                    Modifier
                        .fillMaxSize(),
                    weaponsList = weapons,
                    weaponsValues = weaponsValues
                )

                3 -> RoomsTab(
                    Modifier
                        .fillMaxSize(),
                    roomsList = rooms,
                    roomsValues = roomsValues
                )
            }
        }

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = {
                TabRowDefaults.Indicator(
                    Modifier.size(0.dp),
                    color = Color.Transparent,
                )
            },
            modifier = Modifier
                .weight(0.09f)
                .fillMaxWidth()
        ) {
            repeat(tabTitles.size) {
                Tab(text = { Text(tabTitles[it]) },
                    icon = {
                        Icon(
                            painter = (if (pagerState.currentPage == it) tabIconsSelected[it] else tabIconsNotSelected[it]),
                            contentDescription = null
                        )
                    },
                    selected = pagerState.currentPage == it,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(it) } })
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
            text = "Use this page to hide your notes from peeking eyes 👀",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(350.dp)
        )
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SuspectsTab(
    modifier: Modifier = Modifier,
    suspectList: List<String>,
    suspectsValues: List<Boolean>
) {
    val activeTemplateViewModel = ActiveTemplateViewModel(
        ActiveTemplateRepository(
            TemplateRoomDatabase.getInstance(LocalContext.current).activeTemplateDao()
        )
    )
    val tabValues: MutableList<Boolean> by remember {
        mutableStateOf(suspectsValues.toMutableList())
    }

    ListTab(
        modifier = modifier,
        title = "Suspects",
        items = suspectList,
        tabValues = tabValues,
        dbUpdate = {
            activeTemplateViewModel.updateSuspectsBooleans(tabValues.joinToString())
        }
    )
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun WeaponsTab(
    modifier: Modifier = Modifier,
    weaponsList: List<String>,
    weaponsValues: List<Boolean>
) {
    val activeTemplateViewModel = ActiveTemplateViewModel(
        ActiveTemplateRepository(
            TemplateRoomDatabase.getInstance(LocalContext.current).activeTemplateDao()
        )
    )
    val tabValues: MutableList<Boolean> by remember {
        mutableStateOf(weaponsValues.toMutableList())
    }

    ListTab(
        modifier = modifier,
        title = "Weapons",
        items = weaponsList,
        tabValues = tabValues,
        dbUpdate = {
            activeTemplateViewModel.updateWeaponsBooleans(tabValues.joinToString())
        }
    )
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun RoomsTab(
    modifier: Modifier = Modifier,
    roomsList: List<String>,
    roomsValues: List<Boolean>
) {
    val activeTemplateViewModel = ActiveTemplateViewModel(
        ActiveTemplateRepository(
            TemplateRoomDatabase.getInstance(LocalContext.current).activeTemplateDao()
        )
    )
    val tabValues: MutableList<Boolean> by remember {
        mutableStateOf(roomsValues.toMutableList())
    }

    ListTab(
        modifier = modifier,
        title = "Rooms",
        items = roomsList,
        tabValues = tabValues,
        dbUpdate = {
            activeTemplateViewModel.updateRoomsBooleans(tabValues.joinToString())
        }
    )
}

@Composable
fun ListTab(
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
                .background(Color.LightGray),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 20.dp),
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

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    itemText: String,
    initialValue: Boolean,
    itemValue: () -> Unit
) {
    var textColor by remember {
        if (initialValue) {
            mutableStateOf(Color.Black)
        } else {
            mutableStateOf(Color.LightGray)
        }
    }
    var textDecoration by remember {
        if (initialValue) {
            mutableStateOf(TextDecoration.None)
        } else {
            mutableStateOf(TextDecoration.LineThrough)
        }
    }
    var circleColor by remember {
        if (initialValue) {
            mutableStateOf(Color.Green)
        } else {
            mutableStateOf(Color.LightGray)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(bottom = 5.dp, start = 10.dp, end = 10.dp)
            .clip(RoundedCornerShape(20))
            .background(Color.Yellow)
            .clickable {
                if (textColor == Color.Black) {
                    textColor = Color.LightGray
                    textDecoration = TextDecoration.LineThrough
                    circleColor = Color.LightGray
                    itemValue()
                } else {
                    textColor = Color.Black
                    textDecoration = TextDecoration.None
                    circleColor = Color.Green
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

@Preview(showBackground = true)
@Composable
fun CluedroidPreview() {
    CluedroidTheme {
        CluedroidMain()
    }
}


