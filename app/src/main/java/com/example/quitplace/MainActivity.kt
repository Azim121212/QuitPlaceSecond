package com.example.quitplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.quitplace.ui.navigation.MainApp
import com.example.quitplace.ui.onboarding.OnboardingFlow

// @AndroidEntryPoint  // ЗАКОММЕНТИРОВАНО - ВРЕМЕННО БЕЗ HILT
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppEntryPoint()
                }
            }
        }
    }
}

@Composable
fun AppEntryPoint() {
    var showOnboarding by remember { mutableStateOf(true) }

    if (showOnboarding) {
        OnboardingFlow(
            onComplete = {
                showOnboarding = false
            }
        )
    } else {
        MainApp()
    }
}