package com.example.retrofitlabsandboxapp.mvi.post

import com.example.retrofitlabsandboxapp.NavigationFolder.FilterType
import com.example.retrofitlabsandboxapp.RetrofidFolder.post.PostObject
import com.example.retrofitlabsandboxapp.RetrofidFolder.post.PostWithSendingStatus

data class PostState(
    val filterType: FilterType = FilterType.All,
    val userId: String = "",
    val userTitle: String = "",
    val userBody: String = "",
    val currentExceptions: List<String> = emptyList(),
    val userPosts: List<PostWithSendingStatus> = emptyList(),
    val logs: List<String> = emptyList(),
    val posts: List<PostObject> = emptyList(),
    val restoredPost: PostObject? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val postFormIsOpen: Boolean = false
)
