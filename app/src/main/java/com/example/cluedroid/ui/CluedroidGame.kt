package com.example.cluedroid.ui

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.sharp.Search
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cluedroid.R
import com.example.cluedroid.db.TemplateRoomDatabase
import com.example.cluedroid.repository.ActiveTemplateRepository
import com.example.cluedroid.repository.TemplateRepository
import com.example.cluedroid.view.ActiveTemplateViewModel
import com.example.cluedroid.view.TemplateViewModel
import kotlinx.coroutines.launch

@Composable
fun CluedroidGame(
    navigateToSettings: () -> Unit
) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(), color = backgroundColor()
    ) {
        CluedroidGameMain(navigateToSettings)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CluedroidGameMain(navigateToSettings: () -> Unit) {
    //Get View Models
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
    //Get template and active template
    val template = templateViewModel.findTemplateById(
        activeTemplateViewModel.getActiveTemplateData().activeTemplateIndex.toInt()
    )
    val activeTemplate = activeTemplateViewModel.getActiveTemplateData()
    //Get template data
    val suspects = template.suspects.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    val weapons = template.weapons.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    val rooms = template.rooms.trim().splitToSequence(";").filter { it.isNotEmpty() }.toList()
    //Get active template data (marked/unmarked)
    val suspectsValues =
        activeTemplate.suspectsBooleans.trim().splitToSequence(", ").filter { it.isNotEmpty() }
            .toList().map { it.toBoolean() }
    val weaponsValues =
        activeTemplate.weaponsBooleans.trim().splitToSequence(", ").filter { it.isNotEmpty() }
            .toList().map { it.toBoolean() }
    val roomsValues =
        activeTemplate.roomsBooleans.trim().splitToSequence(", ").filter { it.isNotEmpty() }
            .toList().map { it.toBoolean() }

    val tabTitles = listOf(
        stringResource(R.string.hide_tab_title),
        stringResource(R.string.suspects_tab_title),
        stringResource(R.string.weapons_tab_title),
        stringResource(R.string.rooms_tab_title)
    )

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
            .background(color = backgroundColor()),
    ) {
        TopBar(navigateToSettings)

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
                .fillMaxWidth(),
            containerColor = onPrimaryColor()
        ) {
            repeat(tabTitles.size) {
                Tab(text = { Text(tabTitles[it]) },
                    icon = {
                        Icon(
                            painter = (if (pagerState.currentPage == it) tabIconsSelected[it]
                                else tabIconsNotSelected[it]),
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
private fun TopBar(navigateToSettings: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = onPrimaryColor()
        ),
        title = {
            Text("")
        },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = stringResource(R.string.new_game_description)
                )
            }
        },
        actions = {
            IconButton(onClick = navigateToSettings) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = stringResource(R.string.settings_description)
                )
            }
        }
    )
}

@Composable
private fun HideTab(modifier: Modifier = Modifier) {
    val circleColor = onPrimaryColor()
    val iconColor = primaryColor()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Sharp.Search,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .size(250.dp)
                .drawBehind {
                    drawCircle(
                        color = circleColor,
                        radius = (this.size.width / 1.7).toFloat()
                    )
                }
        )
        Spacer(modifier = Modifier.padding(30.dp))
        Text(
            text = stringResource(R.string.hide_tab_text),
            color = onBackgroundColor(),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(350.dp)
        )
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
private fun SuspectsTab(
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
private fun WeaponsTab(
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
private fun RoomsTab(
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
private fun ListTab(
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
                .background(onPrimaryColor()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 20.dp),
                color = onSurfaceColor(),
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
private fun ListItem(
    modifier: Modifier = Modifier,
    itemText: String,
    initialValue: Boolean,
    itemValue: () -> Unit
) {
    val circleColorValueUnmarked = onPrimaryColor()
    val circleColorValueMarked = onPrimaryContainerColor()

    val fontColorValueMarked = surfaceVariantColor()
    val fontColorValueUnmarked = backgroundColor()

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
            .background(primaryColor())
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
