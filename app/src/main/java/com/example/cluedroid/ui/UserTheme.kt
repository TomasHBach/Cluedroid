package com.example.cluedroid.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.cluedroid.ui.theme.isDarkMode


@Composable
fun backgroundColor() = MaterialTheme.colorScheme.background

@Composable
fun onBackgroundColor() = MaterialTheme.colorScheme.onBackground

@Composable
fun onSurfaceColor() = MaterialTheme.colorScheme.onSurface

@Composable
fun surfaceVariantColor() = MaterialTheme.colorScheme.surfaceVariant

@Composable
fun primaryColor() = MaterialTheme.colorScheme.primary

@Composable
fun onPrimaryColor() = if (isDarkMode()) {
    MaterialTheme.colorScheme.onPrimary
} else {
    MaterialTheme.colorScheme.primaryContainer
}

@Composable
fun onPrimaryContainerColor() = if (isDarkMode()) {
    MaterialTheme.colorScheme.onPrimaryContainer
} else {
    MaterialTheme.colorScheme.surfaceVariant
}
