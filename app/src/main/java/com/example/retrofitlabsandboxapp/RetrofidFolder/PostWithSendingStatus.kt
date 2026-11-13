package com.example.retrofitlabsandboxapp.RetrofidFolder

import com.example.retrofitlabsandboxapp.RetrofidFolder.post.PostObject

data class PostWithSendingStatus(
    val post: PostObject,
    val syncStatus: SyncStatus
)
