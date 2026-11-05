package com.example.retrofitlabsandboxapp.ScreensFolder.PostScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent
import com.example.retrofitlabsandboxapp.mvi.post.PostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenPost(
    modifier: Modifier = Modifier,
    onIntent: (PostIntent) -> Unit,
    state: PostState,
    onBack: () -> Boolean
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Detail screen") },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                        onIntent(PostIntent.UpdateRestoredPost(null))
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(90.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {


                Text(
                    text = "ID: ${state.restoredPost?.userId ?: ""}",
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = state.restoredPost?.title ?: "",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = state.restoredPost?.body ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 24.sp
                    )
                )
            }


        }
    }
}