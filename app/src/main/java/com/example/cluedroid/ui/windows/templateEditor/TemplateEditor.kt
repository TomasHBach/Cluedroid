package com.example.cluedroid.ui.windows.templateEditor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.cluedroid.ui.windows.templateEditor.mainWindows.ReviewWindow
import com.example.cluedroid.ui.windows.templateEditor.mainWindows.TemplateEditorWindow
import kotlinx.coroutines.launch

@Composable
fun TemplateEditor(
    navigateBack: () -> Unit,
    templateName: String,
    updateTemplateName: (String) -> Unit,
    suspectsMutableList: MutableList<String>,
    suspectsMutableBooleanList: MutableList<MutableState<Boolean>>,
    weaponsMutableList: MutableList<String>,
    weaponsMutableBooleanList: MutableList<MutableState<Boolean>>,
    roomsMutableList: MutableList<String>,
    roomsMutableBooleanList: MutableList<MutableState<Boolean>>,
    finishButtonFunc: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ) {
        TemplateEditorMain(
            navigateBack,
            templateName,
            updateTemplateName,
            suspectsMutableList,
            suspectsMutableBooleanList,
            weaponsMutableList,
            weaponsMutableBooleanList,
            roomsMutableList,
            roomsMutableBooleanList,
            finishButtonFunc
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TemplateEditorMain(
    navigateBack: () -> Unit,
    templateName: String,
    updateTemplateName: (String) -> Unit,
    suspectsMutableList: MutableList<String>,
    suspectsMutableBooleanList: MutableList<MutableState<Boolean>>,
    weaponsMutableList: MutableList<String>,
    weaponsMutableBooleanList: MutableList<MutableState<Boolean>>,
    roomsMutableList: MutableList<String>,
    roomsMutableBooleanList: MutableList<MutableState<Boolean>>,
    finishButtonFunc: () -> Unit
) {
    //Show Dialog to Leave
    var openLeaveDialog by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(pageCount = { 2 })
    val subPagerState = rememberPagerState(pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()
    val scrollPositionList = listOf(
        rememberScrollState(),
        rememberScrollState(),
        rememberScrollState()
    )
    val reviewWindowScrollPosition = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar { openLeaveDialog = openLeaveDialog == false }

        if (openLeaveDialog) {
            LeaveDialog(
                onDismissRequest = { openLeaveDialog = false },
                onConfirmation = {
                    openLeaveDialog = false
                    navigateBack()
                }
            )
        }

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> TemplateEditorWindow(
                    scrollPositionList,
                    reviewWindowScrollPosition,
                    pagerState,
                    subPagerState,
                    templateName,
                    updateTemplateName,
                    suspectsMutableList,
                    suspectsMutableBooleanList,
                    weaponsMutableList,
                    weaponsMutableBooleanList,
                    roomsMutableList,
                    roomsMutableBooleanList
                )

                1 -> ReviewWindow(
                    scrollPosition = reviewWindowScrollPosition,
                    templateName = templateName,
                    suspectsList = suspectsMutableList,
                    weaponsList = weaponsMutableList,
                    roomsList = roomsMutableList,
                    backFunction = {
                        coroutineScope.launch {
                            scrollPositionList[2].scrollTo(0)
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    finishButtonFunc = finishButtonFunc
                )
            }
            BackHandler(
                enabled = true,
                onBack = {
                    if (page == 0) {
                        when(subPagerState.currentPage) {
                            0 -> {openLeaveDialog = true}
                            1 -> coroutineScope.launch {
                                subPagerState.animateScrollToPage(0)
                            }
                            else -> coroutineScope.launch {
                                scrollPositionList[subPagerState.currentPage - 2].scrollTo(0)
                                subPagerState.animateScrollToPage(subPagerState.currentPage - 1)
                            }
                        }
                    } else {
                        coroutineScope.launch {
                            scrollPositionList[2].scrollTo(0)
                            pagerState.animateScrollToPage(0)
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(cancelAndLeave: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text(text = "Edit Template", textAlign = TextAlign.Center)
        },
        navigationIcon = {
            IconButton(onClick = cancelAndLeave) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Leave edit mode",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
private fun LeaveDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        title = {
            Text(
                text = "Cancel and discard changes?",
                fontSize = 17.sp
            )
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
