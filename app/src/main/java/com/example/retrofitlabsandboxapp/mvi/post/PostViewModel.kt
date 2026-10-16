package com.example.retrofitlabsandboxapp.mvi.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitlabsandboxapp.NavigationFolder.FilterType
import com.example.retrofitlabsandboxapp.RetrofidFolder.RetrofitInstance
import com.example.retrofitlabsandboxapp.Tags
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.AddLog
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.FetchFilteredPosts
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.SavePost
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.UpdateCurrentId
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.UpdateFilterType
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.UpdateLoadingState
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.UpdateRestoredPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostViewModel : ViewModel() {
    private val _ps = MutableStateFlow(PostState())
    val ps = _ps.asStateFlow()

    fun onIntent(intent: PostIntent) {
        when (intent) {
            is UpdateCurrentId -> {
                _ps.update {
                    it.copy(currentPostId = intent.postId)
                }
            }

            is FetchFilteredPosts -> {
                viewModelScope.launch {
                    onIntent(UpdateLoadingState(true))
                    _ps.update {
                        it.copy(posts = emptyList())
                    }
                    val query = intent.query
                    if (query != null) {
                        try {
                            val response = if (_ps.value.filterType != FilterType.All)
                                RetrofitInstance.api.searchPost(query)
                            else RetrofitInstance.api.getAllPosts()

                            if (response.isSuccessful) {
                                onIntent(UpdateLoadingState(false))
                                _ps.update {
                                    it.copy(
                                        posts = response.body() ?: emptyList()
                                    )
                                }
                            } else {
                                onIntent(UpdateLoadingState(false))
                                onIntent(
                                    AddLog(
                                        Tags
                                            .VIEWMODEL.tag
                                                + "Response is not successful"
                                                + SimpleDateFormat(
                                            "dd.MM.yyyy HH:mm:ss", Locale.getDefault()
                                        )
                                            .format(Date())
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            onIntent(
                                AddLog(
                                    Tags
                                        .VIEWMODEL.tag
                                            + e.message
                                            + SimpleDateFormat(
                                        "dd.MM.yyyy HH:mm:ss", Locale.getDefault()
                                    )
                                        .format(Date())
                                )
                            )
                        }
                    } else {
                        onIntent(
                            AddLog(
                                Tags
                                    .VIEWMODEL.tag
                                        + "Query is blank"
                                        + SimpleDateFormat(
                                    "dd.MM.yyyy HH:mm:ss", Locale.getDefault()
                                )
                                    .format(Date())
                            )
                        )
                    }
                }


            }

            is AddLog -> {
                _ps.update {
                    it.copy(
                        logs = it.logs + intent.log
                    )
                }


            }

            is SavePost -> {
                //Todo("Later to finish writing")
            }

            is UpdateRestoredPost -> {
                _ps.update {
                    it.copy(
                        restoredPost = intent.restoredPost
                    )
                }
            }

            is UpdateFilterType -> {
                _ps.update {
                    it.copy(
                        filterType = intent.filterType
                    )
                }
            }

            is UpdateLoadingState -> {
                _ps.update {
                    it.copy(isLoading = intent.isLoading)
                }
            }
        }
    }

}