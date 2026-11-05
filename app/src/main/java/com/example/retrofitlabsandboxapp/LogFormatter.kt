package com.example.retrofitlabsandboxapp

import com.example.retrofitlabsandboxapp.mvi.post.PostIntent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun writeLog(message: String, tag: Tags) {
    PostIntent.AddLog(
        tag.tagName
                + " $message "
                + SimpleDateFormat(
            "dd.MM.yyyy HH:mm:ss ", Locale.getDefault()
        )
            .format(Date())
    )


}