package com.example.retrofitlabsandboxapp.mvi.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitlabsandboxapp.Logs.Tags
import com.example.retrofitlabsandboxapp.Logs.writeLog
import com.example.retrofitlabsandboxapp.RetrofidFolder.RetrofitInstance
import com.example.retrofitlabsandboxapp.RetrofidFolder.post.PostObject
import com.example.retrofitlabsandboxapp.RetrofidFolder.post.PostWithSendingStatus
import com.example.retrofitlabsandboxapp.RetrofidFolder.post.SyncStatus
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.AddLog
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.CreateUserPost
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.FetchFilteredPosts
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent.UpdateErrorMessage
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

class PostViewModel : ViewModel() {
    private var filteredPostsJob: Job? = null
    private var openPostJob: Job? = null
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
    private fun sendPost(userPost: PostWithSendingStatus) {
        viewModelScope.launch {
            delay(1500L) // Специальная задержка, чтобы было видно статус SENDING
            try {
                val response = RetrofitInstance.postApi.createPost(userPost.post)
                if (response.isSuccessful) {
                    updateSendingState(userPost, SyncStatus.SYNCED)
                    writeLog(
                        "was successful created post: ${response.body().toString()}." +
                                "Code: ${response.code()}", Tags.POST_VIEWMODEL
                    )
                } else {
                    updateSendingState(userPost, SyncStatus.FAILED)
                    writeLog("response isn't successful", Tags.POST_VIEWMODEL)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                updateSendingState(userPost, SyncStatus.FAILED)
                writeLog(e.message.toString(), Tags.POST_VIEWMODEL)

            }


        }
    }
    fun onIntent(intent: PostIntent) {
        when (intent) {
            is FetchFilteredPosts -> {
                if (filteredPostsJob != null) {
                    filteredPostsJob?.cancel()
                }
                onIntent(UpdateErrorMessage(""))
                filteredPostsJob = viewModelScope.launch {

                    onIntent(UpdateLoadingState(true))
                    _postState.update {
                        it.copy(posts = emptyList())

                    }
                    val query: Int? = intent.query
                    try {
                        val response = RetrofitInstance.postApi.searchPost(query)

                        if (response.isSuccessful) {
                            writeLog(
                                "Response is successful", Tags.POST_VIEWMODEL
                            )

                            onIntent(UpdateLoadingState(false))
                            _postState.update {
                                it.copy(
                                    posts = response.body() ?: emptyList(),

                                    )
                            }

                        } else {
                            onIntent(UpdateLoadingState(false))
                            writeLog(response.code().toString(), Tags.POST_VIEWMODEL)
                            onIntent(UpdateErrorMessage(response.code().toString()))


                        }
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        writeLog(e.message.toString(), Tags.POST_VIEWMODEL)
                        onIntent(UpdateLoadingState(false))
                        onIntent(UpdateErrorMessage(e.message.toString()))


                    } finally {
                        writeLog("Response was finished", Tags.POST_VIEWMODEL)


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
                sendPost(userPost)
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
                onIntent(UpdateErrorMessage(""))

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
                    delay(1000L) //Специальная задержка для демонстративности
                    try {

                        val response = RetrofitInstance.postApi.openPost(intent.postId)

                        if (response.isSuccessful) {

                            onIntent(UpdateLoadingState(false))
                            _postState.update {
                                it.copy(restoredPost = response.body())
                            }
                        } else {
                            onIntent(UpdateLoadingState(false))
                            writeLog(response.code().toString(), Tags.POST_VIEWMODEL)
                            onIntent(UpdateErrorMessage(response.code().toString()))


                        }
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        writeLog(e.message.toString(), Tags.POST_VIEWMODEL)
                        onIntent(UpdateLoadingState(false))
                        onIntent(UpdateErrorMessage(e.message.toString()))


                    } finally {
                        writeLog("Response was finished", Tags.POST_VIEWMODEL)
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
                updateSendingState(intent.post, SyncStatus.SENDING)
                sendPost(intent.post)
            }

            is UpdateErrorMessage -> {
                _postState.update {
                    it.copy(errorMessage = intent.errorMessage)
                }
            }
        }

    }
}


