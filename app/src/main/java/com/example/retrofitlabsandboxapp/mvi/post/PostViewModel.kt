package com.example.retrofitlabsandboxapp.mvi.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitlabsandboxapp.RetrofidFolder.PostWithSendingStatus
import com.example.retrofitlabsandboxapp.RetrofidFolder.RetrofitInstance
import com.example.retrofitlabsandboxapp.RetrofidFolder.SyncStatus
import com.example.retrofitlabsandboxapp.RetrofidFolder.post.PostObject
import com.example.retrofitlabsandboxapp.Tags
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.AddLog
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.CreateUserPost
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.FetchFilteredPosts
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.UpdateFilterType
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.UpdateLoadingState
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.UpdateRestoredPost
import com.example.retrofitlabsandboxapp.writeLog
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private var filteredPostsJob: Job? = null
    private var openPostJob: Job? = null
    private var createPostJob: Job? = null
    private val _postState = MutableStateFlow(PostState())
    val postState = _postState.asStateFlow()
//    private val _postsState = MutableStateFlow(emptyList<PostObject>())
//        val postsState = _postState.asStateFlow()

    var countId: Int = 0
    private fun updateSendingState(
        post: PostWithSendingStatus,
        status: SyncStatus
    ) {
        _postState.update { state ->
            state.copy(
                userPosts = state.userPosts.map { statePost ->
                    if (statePost.post.id == post.post.id)
                        statePost.copy(
                            syncStatus = status
                        )
                    else statePost
                }
            )
        }
    }
    fun onIntent(intent: PostIntent) {
        when (intent) {


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
                            writeLog(
                                "Response is successful", Tags.VIEWMODEL
                            )

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

            is CreateUserPost -> {
                val userPost = PostWithSendingStatus(
                    PostObject(
                        body = _postState.value.userBody,
                        id = --countId,
                        title = _postState.value.userTitle,
                        userId = _postState.value.userId.toInt()
                    ),
                    syncStatus = SyncStatus.SENDING
                )
                _postState.update {
                    it.copy(
                        userPosts = it.userPosts + userPost
                    )
                }
                createPostJob?.cancel()
                createPostJob = viewModelScope.launch {

                    try {
                        val response = RetrofitInstance.api.createPost(userPost.post)
                        if (response.isSuccessful) {
                            updateSendingState(userPost, SyncStatus.SYNCED)
                            writeLog(
                                "was successful created post: ${response.body().toString()}." +
                                        "Code: ${response.code()}", Tags.VIEWMODEL
                            )
                        } else {
                            updateSendingState(userPost, SyncStatus.FAILED)
                            writeLog("response isn't successful", Tags.VIEWMODEL)
                        }
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        updateSendingState(userPost, SyncStatus.FAILED)
                        writeLog(e.message.toString(), Tags.VIEWMODEL)

                    }


                }


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
                openPostJob?.cancel()

                // Отрицательный id — это фиктивный пост
                if (intent.postId < 0) {
                    _postState.update {
                        it.copy(
                            restoredPost = null,
                            isLoading = false
                        )
                    }
                    return
                }

                onIntent(UpdateLoadingState(true))
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

            is PostIntent.UpdatePostFormFlag -> {
                _postState.update {
                    it.copy(postFormIsOpen = intent.postFormIsOpen)
                }
            }

            is PostIntent.UpdateUserBody -> {
                _postState.update {
                    it.copy(
                        userBody = intent.userBody
                    )
                }
            }

            is PostIntent.UpdateUserId -> {
                _postState.update {
                    it.copy(
                        userId = intent.userId
                    )
                }
            }

            is PostIntent.UpdateUserTitle -> {
                _postState.update {
                    it.copy(
                        userTitle = intent.userTitle
                    )
                }
            }

            PostIntent.ClearUserForm -> {
                _postState.update {
                    it.copy(
                        userId = "",
                        userBody = "",
                        userTitle = ""
                    )
                }
            }

            is PostIntent.RestartSendingFailedPost -> {
                viewModelScope.launch {
                    val userPost = intent.post
                    delay(1000L)
                    try {
                        val response = RetrofitInstance.api.createPost(userPost.post)
                        if (response.isSuccessful) {
                            updateSendingState(userPost, SyncStatus.SYNCED)
                            writeLog(
                                "was successful created post: ${response.body().toString()}." +
                                        "Code: ${response.code()}", Tags.VIEWMODEL
                            )
                        } else {
                            updateSendingState(userPost, SyncStatus.FAILED)
                            writeLog("response isn't successful", Tags.VIEWMODEL)
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


