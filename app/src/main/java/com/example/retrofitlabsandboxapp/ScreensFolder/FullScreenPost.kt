package com.example.retrofitlabsandboxapp.ScreensFolder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    LaunchedEffect(state.currentPostId) {
        if (state.currentPostId != -1) {
            val restoredPost = state.posts.find { it.id == state.currentPostId }
            onIntent(PostIntent.UpdateRestoredPost(restoredPost))
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(state.currentTitle) },
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
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