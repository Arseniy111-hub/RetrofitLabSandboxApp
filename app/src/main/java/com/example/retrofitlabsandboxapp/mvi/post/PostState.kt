package com.example.retrofitlabsandboxapp.mvi.post

import com.example.retrofitlabsandboxapp.NavigationFolder.FilterType
import com.example.retrofitlabsandboxapp.RetrofidFolder.post.PostObject

data class PostState(
    val currentPostId: Int = -1,
    val filterType: FilterType = FilterType.All,
    val currentUserId: Int = -1,
    val currentTitle: String = "",
    val currentBody: String = "",
    val currentExceptions: List<String> = emptyList(),
    val userPosts: List<PostObject> = emptyList(),
    val logs: List<String> = emptyList(),
    val posts: List<PostObject> = emptyList(),
    val restoredPost: PostObject? = null,
    val isLoading: Boolean = false
)
