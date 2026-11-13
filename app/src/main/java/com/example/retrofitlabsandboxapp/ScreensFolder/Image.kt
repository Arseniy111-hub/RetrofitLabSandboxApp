package com.example.retrofitlabsandboxapp.ScreensFolder

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Image screen")
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues)
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
                            placeholder = { Text("Поиск картинок") }
                        )
                    },
                    onExpandedChange = { expandedSearchBarState = it },
                    content = {
                        //TODO("Her will be history of query")
                    }
                )
            }
        }
    }
}