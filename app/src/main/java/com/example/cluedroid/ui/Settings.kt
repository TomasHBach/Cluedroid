package com.example.cluedroid.ui

import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cluedroid.db.TemplateRoomDatabase
import com.example.cluedroid.repository.UserSettingsRepository
import com.example.cluedroid.view.UserSettingsViewModel
import com.example.cluedroid.model.Theme

@Composable
fun Settings(
    navigateBack: () -> Unit
) {
// A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        SettingsMain(navigateBack)
    }
}

@Composable
private fun SettingsMain(navigateBack: () -> Unit) {
    val userSettingsViewModel = UserSettingsViewModel(
        UserSettingsRepository(
            TemplateRoomDatabase.getInstance(LocalContext.current).userSettingsDao()
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        TopBar(navigateBack)
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Change theme",
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(start = 15.dp, top = 20.dp, bottom = 20.dp)
                )
                ThemeDropdownMenu(userSettingsViewModel)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigateBack: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text(text = "Settings")
        },
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Go back")
            }
        }
    )
}

@Composable
private fun ListItem(text: String, func: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { func() }
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(start = 15.dp, top = 20.dp, bottom = 20.dp)
        )
        func()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeDropdownMenu(viewModel: UserSettingsViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Auto", "Light", "Dark")
    var selectedOptionText by remember {
        mutableStateOf(
            when (AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> options[0]
                AppCompatDelegate.MODE_NIGHT_NO -> options[1]
                AppCompatDelegate.MODE_NIGHT_YES -> options[2]
                else -> options[0]
            }
        )
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .width(130.dp)
            .padding(end = 10.dp)
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            textStyle = TextStyle.Default.copy(fontSize = 20.sp),
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                focusedIndicatorColor = Color.Transparent
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        //Update text
                        AppCompatDelegate.setDefaultNightMode(
                            when (selectedOptionText) {
                                options[0] -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                                options[1] -> AppCompatDelegate.MODE_NIGHT_NO
                                options[2] -> AppCompatDelegate.MODE_NIGHT_YES
                                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                            }
                        )
                        //Update settings database
                        when (selectedOptionText) {
                            options[0] -> viewModel.updateTheme(Theme.AUTO.toString())
                            options[1] -> viewModel.updateTheme(Theme.LIGHT.toString())
                            options[2] -> viewModel.updateTheme(Theme.DARK.toString())
                            else -> viewModel.updateTheme(Theme.AUTO.toString())
                        }
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    modifier = Modifier.background(Color.Transparent)
                )
            }
        }
    }
}
