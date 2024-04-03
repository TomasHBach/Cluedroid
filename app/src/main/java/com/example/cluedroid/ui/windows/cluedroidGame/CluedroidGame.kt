package com.example.cluedroid.ui.windows.cluedroidGame

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cluedroid.R
import com.example.cluedroid.db.TemplateRoomDatabase
import com.example.cluedroid.repository.ActiveTemplateRepository
import com.example.cluedroid.repository.TemplateRepository
import com.example.cluedroid.view.ActiveTemplateViewModel
import com.example.cluedroid.view.TemplateViewModel
import kotlinx.coroutines.launch

@Composable
fun CluedroidGame(
    navigateToSettings: () -> Unit,
    navigateToStartGame: () -> Unit
) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ) {
        CluedroidGameMain(navigateToSettings, navigateToStartGame)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CluedroidGameMain(
    navigateToSettings: () -> Unit,
    navigateToStartGame: () -> Unit
) {
    //Show Dialog to Reset
    var openResetDialog by remember { mutableStateOf(false) }

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
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        TopBar(navigateToSettings) { openResetDialog = openResetDialog == false }

        if (openResetDialog) {
            ResetDialog(
                onDismissRequest = { openResetDialog = false },
                onConfirmation = {
                    openResetDialog = false
                    navigateToStartGame()
                }
            )
        }

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
            containerColor = MaterialTheme.colorScheme.primaryContainer
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
private fun TopBar(navigateToSettings: () -> Unit, openResetDialog: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text("")
        },
        navigationIcon = {
            IconButton(onClick = openResetDialog) {
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
    val iconColor = MaterialTheme.colorScheme.onPrimaryContainer
    val circleColor = MaterialTheme.colorScheme.primaryContainer

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
            color = MaterialTheme.colorScheme.onSurface,
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

    TabList(
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

    TabList(
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

    TabList(
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
fun ResetDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        text = {
            Text(text = "Do you want to finish this game?")
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("No")
            }
        }
    )
}
