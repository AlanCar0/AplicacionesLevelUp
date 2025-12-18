package com.upgamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.upgamer.ui.navigation.AppNavigation
import com.upgamer.ui.theme.UpGamerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UpGamerTheme {
                AppNavigation()
            }
        }
    }
}