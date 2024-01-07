package com.example.cluedroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cluedroid.ui.CluedroidGame
import com.example.cluedroid.ui.theme.CluedroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CluedroidTheme {
                CluedroidGame()
            }
        }
    }
}
