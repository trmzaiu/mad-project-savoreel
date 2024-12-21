package com.example.savoreel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.savoreel.ui.AppNavigation
import com.example.savoreel.ui.theme.SavoreelTheme
import com.example.savoreel.ui.theme.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SavoreelTheme(dynamicColor = false) {
                val themeViewModel: ThemeViewModel = viewModel()
                SavoreelTheme(darkTheme = themeViewModel.isDarkModeEnabled) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        val navController = rememberNavController()
                        // Pass the existing themeViewModel to AppNavigation
                        AppNavigation(navController = navController, themeViewModel = themeViewModel)
                    }
                }
            }
        }
    }
}


