package com.example.retrofitlabsandboxapp.mvi.post

import com.example.retrofitlabsandboxapp.NavigationFolder.FilterType
import com.example.retrofitlabsandboxapp.RetrofidFolder.post.PostObject

sealed interface PostIntent {
    data class UpdateCurrentId(val postId: Int) : PostIntent
    data class FetchFilteredPosts(val query: Int?) : PostIntent
    data class AddLog(val log: String) : PostIntent
    data class SavePost(val post: PostObject) : PostIntent
    data class UpdateRestoredPost(val restoredPost: PostObject?) : PostIntent
    data class UpdateFilterType(val filterType: FilterType) : PostIntent
    data class UpdateLoadingState(val isLoading: Boolean) : PostIntent

}
