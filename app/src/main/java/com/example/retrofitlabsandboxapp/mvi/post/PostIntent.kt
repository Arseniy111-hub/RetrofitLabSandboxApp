package com.example.retrofitlabsandboxapp.mvi.post

import com.example.retrofitlabsandboxapp.NavigationFolder.FilterType
import com.example.retrofitlabsandboxapp.RetrofidFolder.PostWithSendingStatus
import com.example.retrofitlabsandboxapp.RetrofidFolder.post.PostObject

sealed interface PostIntent {

    data class FetchFilteredPosts(val query: Int?) : PostIntent
    data class AddLog(val log: String) : PostIntent
    object CreateUserPost : PostIntent
    data class UpdateRestoredPost(val restoredPost: PostObject?) : PostIntent
    data class UpdateFilterType(val filterType: FilterType) : PostIntent
    data class UpdateLoadingState(val isLoading: Boolean) : PostIntent
    data class FetchPostOnId(val postId: Int) : PostIntent
    data class UpdatePostFormFlag(val postFormIsOpen: Boolean) : PostIntent
    data class UpdateUserTitle(val userTitle: String) : PostIntent
    data class UpdateUserBody(val userBody: String) : PostIntent
    data class UpdateUserId(val userId: String) : PostIntent
    object ClearUserForm : PostIntent
    data class RestartSendingFailedPost(val post: PostWithSendingStatus) : PostIntent
    data class UpdateErrorMessage(val errorMessage: String) : PostIntent

}
