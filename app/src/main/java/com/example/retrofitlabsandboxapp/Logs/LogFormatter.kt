package com.example.retrofitlabsandboxapp.Logs

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LogFormatter {
    private val _logsRepository = MutableStateFlow(emptyList<String>())
    val logsRepository = _logsRepository.asStateFlow()

    fun writeLog(message: String, tag: Tags) {
        val log = tag.tagName + " $message " + SimpleDateFormat(
            "dd.MM.yyyy HH:mm:ss ",
            Locale.getDefault()
        ).format(Date())
        _logsRepository.update {
            it + log
        }
    }
}

fun writeLog(message: String, tag: Tags) {
    LogFormatter.writeLog(message, tag)
}
