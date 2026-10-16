package com.example.retrofitlabsandboxapp.ScreensFolder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Weather(modifier: Modifier = Modifier) {
    Scaffold { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues)
        ) {
            Box(
                contentAlignment = Alignment.TopCenter
            ) {
                Text("Weather screen")
            }
        }
    }
}