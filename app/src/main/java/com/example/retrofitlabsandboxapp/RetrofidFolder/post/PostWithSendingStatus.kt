package com.example.retrofitlabsandboxapp.RetrofidFolder.post

data class PostWithSendingStatus(
    val post: PostObject,
    val syncStatus: SyncStatus
)