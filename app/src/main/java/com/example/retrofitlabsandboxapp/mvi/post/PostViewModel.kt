package com.example.retrofitlabsandboxapp.mvi.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitlabsandboxapp.RetrofidFolder.RetrofitInstance
import com.example.retrofitlabsandboxapp.Tags
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.AddLog
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.FetchFilteredPosts
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.SavePost
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.UpdateCurrentId
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.UpdateFilterType
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.UpdateLoadingState
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.UpdateRestoredPost
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostViewModel : ViewModel() {
    private var filteredPostsJob: Job? = null
    private var openPostJob: Job? = null
    private val _postState = MutableStateFlow(PostState())
    val ps = _postState.asStateFlow()

    private fun writeLog(message: String, tag: Tags) {
        onIntent(
            AddLog(
                tag.tagName
                        + " $message "
                        + SimpleDateFormat(
                    "dd.MM.yyyy HH:mm:ss ", Locale.getDefault()
                )
                    .format(Date())
            )
        )

    }

    fun onIntent(intent: PostIntent) {
        when (intent) {
            is UpdateCurrentId -> {
                _postState.update {
                    it.copy(currentPostId = intent.postId)
                }
            }

            is FetchFilteredPosts -> {
                if (filteredPostsJob != null) {
                    filteredPostsJob?.cancel()
                }
                filteredPostsJob = viewModelScope.launch {

                    onIntent(UpdateLoadingState(true))
                    _postState.update {
                        it.copy(posts = emptyList(), errorMessage = "")

                    }
                    val query: Int? = intent.query
                    try {


                        val response = RetrofitInstance.api.searchPost(query)

                        if (response.isSuccessful) {
                            writeLog("Response is successful", Tags.VIEWMODEL)

                            onIntent(UpdateLoadingState(false))
                            _postState.update {
                                it.copy(
                                    posts = response.body() ?: emptyList(),

                                    )
                            }

                        } else {
                            onIntent(UpdateLoadingState(false))
                            writeLog("Response is not successful", Tags.VIEWMODEL)
                            _postState.update {
                                it.copy(errorMessage = "Response is not successful")
                            }


                        }
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        writeLog(e.message.toString(), Tags.VIEWMODEL)
                        onIntent(UpdateLoadingState(false))
                        _postState.update {
                            it.copy(errorMessage = e.message.toString())
                        }


                    } finally {
                        writeLog("Response was finished", Tags.VIEWMODEL)


                    }
                }



            }

            is AddLog -> {
                _postState.update {
                    it.copy(
                        logs = it.logs + intent.log
                    )
                }


            }

            is SavePost -> {
                //Todo("Later to finish writing")
            }

            is UpdateRestoredPost -> {
                _postState.update {
                    it.copy(
                        restoredPost = intent.restoredPost
                    )
                }
            }

            is UpdateFilterType -> {
                _postState.update {
                    it.copy(
                        filterType = intent.filterType
                    )
                }
            }

            is UpdateLoadingState -> {
                _postState.update {
                    it.copy(isLoading = intent.isLoading)
                }
            }

            is PostIntent.FetchPostOnId -> {
                onIntent(UpdateLoadingState(true))
                openPostJob?.cancel()
                _postState.update {
                    it.copy(restoredPost = null)
                }

                openPostJob = viewModelScope.launch {
                    delay(300L) //Специальная задержка для демонстративности
                    try {
                        val response = RetrofitInstance.api.openPost(intent.postId)
                        if (response.isSuccessful) {

                            onIntent(UpdateLoadingState(false))
                            _postState.update {
                                it.copy(restoredPost = response.body())
                            }
                        } else {
                            onIntent(UpdateLoadingState(false))
                            writeLog("Response is not successful", Tags.VIEWMODEL)
                            _postState.update {
                                it.copy(errorMessage = "Response is not successful")
                            }


                        }
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        writeLog(e.message.toString(), Tags.VIEWMODEL)
                        onIntent(UpdateLoadingState(false))
                        _postState.update {
                            it.copy(errorMessage = e.message.toString())
                        }


                    } finally {
                        writeLog("Response was finished", Tags.VIEWMODEL)


                    }

                }

            }

        }

    }
        }


