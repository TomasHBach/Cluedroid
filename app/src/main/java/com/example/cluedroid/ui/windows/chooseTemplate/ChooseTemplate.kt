package com.example.cluedroid.ui.windows.chooseTemplate

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
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.cluedroid.R
import com.example.cluedroid.db.TemplateRoomDatabase
import com.example.cluedroid.repository.TemplateRepository
import com.example.cluedroid.view.TemplateViewModel
import kotlinx.coroutines.launch

@Composable
fun ChooseTemplate(
    navigateBack: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToEditTemplate: () -> Unit,
    navigateToCreateTemplate: () -> Unit,
    selectedTemplateId: Int,
    updateEditSelectedTemplateId: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ) {
        ChooseTemplateMain(
            navigateBack,
            navigateToSettings,
            navigateToEditTemplate,
            navigateToCreateTemplate,
            selectedTemplateId,
            updateEditSelectedTemplateId
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChooseTemplateMain(
    navigateBack: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToEditTemplate: () -> Unit,
    navigateToCreateTemplate: () -> Unit,
    selectedTemplateId: Int,
    updateEditSelectedTemplateId: (Int) -> Unit
) {
    //Get View Models
    val templateViewModel = TemplateViewModel(
        TemplateRepository(
            TemplateRoomDatabase.getInstance(LocalContext.current).templateDao()
        )
    )
    //Get list of all templates
    val templatesList = templateViewModel.getAllTemplatesIdName()

    val tabTitles = listOf(
        stringResource(R.string.suspects_tab_title),
        stringResource(R.string.weapons_tab_title),
        stringResource(R.string.rooms_tab_title)
    )
    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
    val coroutineScope = rememberCoroutineScope()
    val infoScrollPosition = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(navigateToSettings)
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> TemplateList(
                    templateList = templatesList,
                    changeSelectedTemplate = {
                        updateEditSelectedTemplateId(it)
                    },
                    viewTemplateInfoFunction = {
                        coroutineScope.launch {
                            infoScrollPosition.scrollTo(0)
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    navigateToCreateTemplate = navigateToCreateTemplate
                )

                1 -> TemplateInfo(
                    templateId = selectedTemplateId,
                    infoScrollPosition = infoScrollPosition,
                    backFunction = { coroutineScope.launch { pagerState.animateScrollToPage(0) } },
                    navigateToStartGame = navigateBack,
                    navigateToEditTemplate = navigateToEditTemplate,
                    updateEditSelectedTemplateId = updateEditSelectedTemplateId,
                    deleteTemplate = navigateBack
                )
            }
            BackHandler(
                enabled = true,
                onBack = {
                    if (page == 0) {
                        navigateBack()
                    } else {
                        coroutineScope.launch { pagerState.animateScrollToPage(0) }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigateToSettings: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text("")
        },
        actions = {
            IconButton(onClick = navigateToSettings) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = stringResource(R.string.settings_description),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}
