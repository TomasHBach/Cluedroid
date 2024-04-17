package com.example.cluedroid.ui.windows.templateEditor.mainWindows

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cluedroid.ui.windows.templateEditor.mainWindows.subWindows.TemplateNameWindow
import com.example.cluedroid.ui.windows.templateEditor.mainWindows.subWindows.TextFieldWindow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TemplateEditorWindow(
    scrollPositionList: List<ScrollState>,
    reviewWindowScrollPosition: ScrollState,
    parentPagerState: PagerState,
    subPagerState: PagerState,
    templateName: String,
    updateTemplateName: (String) -> Unit,
    suspectsMutableList: MutableList<String>,
    suspectsMutableBooleanList: MutableList<MutableState<Boolean>>,
    weaponsMutableList: MutableList<String>,
    weaponsMutableBooleanList: MutableList<MutableState<Boolean>>,
    roomsMutableList: MutableList<String>,
    roomsMutableBooleanList: MutableList<MutableState<Boolean>>
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = subPagerState,
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> TemplateNameWindow(
                    templateName,
                    updateTemplateName
                )

                1 -> TextFieldWindow(
                    scrollPosition = scrollPositionList[0],
                    title = "Suspect",
                    mutableList = suspectsMutableList,
                    mutableBooleanList = suspectsMutableBooleanList,
                    backButtonFunc = {
                        coroutineScope.launch {
                            subPagerState.animateScrollToPage(subPagerState.currentPage - 1)
                        }
                    }
                )

                2 -> TextFieldWindow(
                    scrollPosition = scrollPositionList[1],
                    title = "Weapon",
                    mutableList = weaponsMutableList,
                    mutableBooleanList = weaponsMutableBooleanList,
                    backButtonFunc = {
                        coroutineScope.launch {
                            scrollPositionList[0].scrollTo(0)
                            subPagerState.animateScrollToPage(subPagerState.currentPage - 1)
                        }
                    }
                )

                3 -> TextFieldWindow(
                    scrollPosition = scrollPositionList[2],
                    title = "Room",
                    mutableList = roomsMutableList,
                    mutableBooleanList = roomsMutableBooleanList,
                    backButtonFunc = {
                        coroutineScope.launch {
                            scrollPositionList[1].scrollTo(0)
                            subPagerState.animateScrollToPage(subPagerState.currentPage - 1)
                        }
                    }
                )
            }
        }
        Button(
            modifier = Modifier
                .weight(0.23f)
                .height(100.dp)
                .fillMaxWidth()
                .padding(20.dp),
            onClick = {
                if (subPagerState.currentPage == 3) {
                    coroutineScope.launch {
                        reviewWindowScrollPosition.scrollTo(0)
                        parentPagerState.animateScrollToPage(1)
                        delay(500)
                        trimMutableLists(roomsMutableList, roomsMutableBooleanList)
                    }
                } else {
                    coroutineScope.launch {
                        scrollPositionList[subPagerState.currentPage].scrollTo(0)
                        subPagerState.animateScrollToPage(subPagerState.currentPage + 1)
                    }
                    when (subPagerState.currentPage) {
                        1 -> {
                            coroutineScope.launch {
                                delay(500)
                                trimMutableLists(suspectsMutableList, suspectsMutableBooleanList)
                            }
                        }

                        2 -> {
                            coroutineScope.launch {
                                delay(500)
                                trimMutableLists(weaponsMutableList, weaponsMutableBooleanList)
                            }
                        }
                    }
                }
            },
            enabled = when (subPagerState.currentPage) {
                0 -> templateName.isNotEmpty()
                1 -> isListValid(suspectsMutableList, suspectsMutableBooleanList)
                2 -> isListValid(weaponsMutableList, weaponsMutableBooleanList)
                3 -> isListValid(roomsMutableList, roomsMutableBooleanList)
                else -> false
            },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = "Next",
                fontSize = 35.sp
            )
        }
    }
}

private fun isListValid(
    mutableList: MutableList<String>,
    mutableBooleanList: MutableList<MutableState<Boolean>>
): Boolean {
    mutableList.forEachIndexed { index, item ->
        if (mutableBooleanList[index].value) {
            if (item.isEmpty() || item.contains(";")) {
                return false
            }
        }
    }
    return true
}

private fun trimMutableLists(
    mutableList: MutableList<String>,
    mutableBooleanList: MutableList<MutableState<Boolean>>
) {
    val size = mutableList.size
    for (i in (0 until size).reversed()) {
        if (!mutableBooleanList[i].value) {
            mutableList.removeAt(i)
            mutableBooleanList.removeAt(i)
        }
    }
}
