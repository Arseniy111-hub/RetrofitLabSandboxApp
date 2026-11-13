package com.example.retrofitlabsandboxapp.ScreensFolder.PostScreens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.RestartAlt
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrofitlabsandboxapp.RetrofidFolder.SyncStatus
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent
import com.example.retrofitlabsandboxapp.mvi.post.PostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenPost(
    postId: Int?,
    modifier: Modifier = Modifier,
    onIntent: (PostIntent) -> Unit,
    state: PostState,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val userPost = state.userPosts.find { it.post.id == postId }
    val post = userPost?.post ?: state.restoredPost
    BackHandler { onBack() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Detail screen ${userPost?.syncStatus}") },
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
                .padding(paddingValues)
        ) {


            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier.padding(12.dp)
                ) {
                    if (state.errorMessage.isNotBlank()) {
                        Text(
                            text = state.errorMessage
                        )
                    }
                }
                Box(contentAlignment = Alignment.Center) {
                    if (userPost?.syncStatus == SyncStatus.FAILED) {
                        IconButton(
                            onClick = {
                                onIntent(PostIntent.RestartSendingFailedPost(userPost))
                                Toast.makeText(context, "Reset query...", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(60.dp)
                        ) {
                            Icon(
                                Icons.Default.RestartAlt,
                                "restart sending post"
                            )
                        }
                    }
                }

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
                    text = "User id: ${post?.userId ?: ""}",
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = post?.title ?: "",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = post?.body ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 24.sp
                    )
                )
            }


        }
    }
}