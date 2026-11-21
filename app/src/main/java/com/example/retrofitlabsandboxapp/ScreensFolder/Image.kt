package com.example.retrofitlabsandboxapp.ScreensFolder

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.retrofitlabsandboxapp.Logs.Tags
import com.example.retrofitlabsandboxapp.Logs.writeLog
import com.example.retrofitlabsandboxapp.mvi.image.ImageIntent
import com.example.retrofitlabsandboxapp.mvi.image.ImageState

@SuppressLint("ViewModelConstructorInComposable")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Image(
    modifier: Modifier = Modifier,
    state: ImageState,
    onIntent: (ImageIntent) -> Unit
) {
    //var isActiveDialog by rememberSaveable{ mutableStateOf(false) }

    if (state.image != null) {
        ImageDetailDialog(
            image = state.image,
            onDismiss = {
                onIntent(ImageIntent.CloseImage)
            }
        )
    }





    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Image screen")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                onIntent(ImageIntent.PrevPage(state.currentPage))
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowLeft,
                                "prev page icon"
                            )
                        }
                        Text(
                            "${state.currentPage}",
                            fontSize = 12.sp
                        )
                    }
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ){
////                        TextField(
////                            value = state.currentPage.toString(),
////                            onValueChange = { page -> onIntent(ImageIntent.ManualPageInput(page))}
////                        )
//                        Text(
//                            "${state.currentPage}",
//                            fontSize = 12.sp
//                        )
//                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = {
                                onIntent(ImageIntent.NextPage(state.currentPage))
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowRight,
                                "next page icon"
                            )
                        }
//                        Text(
//                            "${state.currentPage + 1}",
//                            fontSize = 12.sp
//
//                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.TopCenter
            ) {
                var expandedSearchBarState by rememberSaveable { mutableStateOf(false) }
                SearchBar(
                    expanded = expandedSearchBarState,
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = state.query,
                            onQueryChange = { query -> onIntent(ImageIntent.UpdateQuery(query)) },
                            onSearch = {
                                onIntent(ImageIntent.OnSearchQuery(query = state.query))
                                expandedSearchBarState = false
                            },
                            expanded = expandedSearchBarState,
                            onExpandedChange = { expandedSearchBarState = it },
                            placeholder = { Text("Поиск картинок") },
                            leadingIcon = {
                                IconButton(
                                    onClick = {
                                        onIntent(ImageIntent.OnSearchQuery(state.query))
                                        expandedSearchBarState = false
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Search,
                                        "Search icon"
                                    )
                                }

                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        onIntent(ImageIntent.UpdateQuery(""))
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Clear,
                                        "Clear all text"
                                    )

                                }
                            }
                        )
                    },
                    onExpandedChange = { expandedSearchBarState = it },
                    content = {
                        //TODO("Her will be history of query")
                    }
                )
            }

            if (state.error.isNotEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Box {
                        Icon(
                            Icons.Default.Search,
                            "Search icon",
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    Box {
                        Text(
                            state.error
                        )
                    }

                }

            }


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(10.dp),

                ) {
                Text(
                    "Counter images: " + state.images.size.toString()
                )
                if (state.isLoading) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(70.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.secondary
                        )
                    }
                }


                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(160.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalItemSpacing = 6.dp
                ) {

                    items(state.images, key = { it.id }) { image ->
                        Box(
                            modifier = Modifier
                                .animateItem()
                                .clickable(
                                    onClick = {
                                        onIntent(ImageIntent.SelectImage(image))


                                    }
                                )


                        ) {
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = image.webFormatURL,
                                contentDescription = "Each url",
                                onError = { error ->
                                    writeLog(
                                        "${error.result.throwable.message}",
                                        Tags.IMAGE_VIEWMODEL
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}