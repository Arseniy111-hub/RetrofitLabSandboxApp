package com.example.retrofitlabsandboxapp.ScreensFolder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrofitlabsandboxapp.NavigationFolder.FilterType
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent
import com.example.retrofitlabsandboxapp.mvi.post.PostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Post(
    modifier: Modifier = Modifier,
    onIntent: (PostIntent) -> Unit,
    state: PostState,
    onFullScreen: (postId: Int) -> Unit,
    onBack: () -> Unit
) {
    val rememberScroll = rememberScrollState()
    val startFilterType = FilterType.All
    var rememberTabIndex by rememberSaveable { mutableIntStateOf(startFilterType.ordinal) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onIntent(PostIntent.FetchFilteredPosts(state.filterType.ordinal))
                }
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    "Start response"
                )
            }
        },
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScroll)
                    ) {
                        FilterType.entries.forEachIndexed { index, type ->
                            Tab(
                                modifier = Modifier.padding(5.dp),
                                onClick = {
                                    onIntent(PostIntent.UpdateFilterType(type))
                                    rememberTabIndex = index
                                },
                                selected = index == rememberTabIndex
                            ) {
                                Text(
                                    type.name,
                                    fontSize = 12.sp
                                )
                            }
                        }

                    }
                }
            )
        }

    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(70.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.secondary
                )
            }

        }

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 5.dp,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = paddingValues.calculateTopPadding()
            ),
            horizontalArrangement = Arrangement.spacedBy(5.dp)

        ) {
            items(state.posts, key = { it.id }) { post ->
                Card(
                    modifier = Modifier
                        .clickable(
                            onClick = {

                                onFullScreen(post.id)
                            }
                        )
                        .size(width = 200.dp, height = 100.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        ) {
                            Text("User id: " + post.userId.toString())

                        }
                        Box(
                            //contentAlignment = Alignment.TopCenter,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp),

                            ) {
                            Text(
                                "Title:\n" + post.title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }


                }
            }
        }
    }
}

