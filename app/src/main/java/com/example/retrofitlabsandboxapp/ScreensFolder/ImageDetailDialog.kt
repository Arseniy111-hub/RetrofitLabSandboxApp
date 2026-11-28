package com.example.retrofitlabsandboxapp.ScreensFolder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.retrofitlabsandboxapp.RetrofidFolder.image.PixabayImage

@Composable
fun ImageDetailDialog(
    image: PixabayImage?,
    onDismiss: () -> Unit
) {
    Dialog(
        // Вызывается при системном «назад»
        onDismissRequest = onDismiss,
        // false — диалог на всю ширину экрана, а не маленьким окном по центру
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    // чтобы содержимое не залезало под статус-бар и полоску навигации
                    .safeDrawingPadding()
            ) {
                // Верхняя панель с кнопкой закрытия
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close image")
                    }
                }
                if (image != null) {
                    // Место под картинку: занимает всё свободное пространство
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = image.largeImageURL,
                            contentDescription = "full size image",
                        )

                    }

                    // Место под информацию: лайки, просмотры, автор, теги
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column {
                            Icon(
                                Icons.Default.HeartBroken,
                                "Like icon"
                            )
                            Text(
                                "${image.likes}"
                            )
                        }
                        Column {
                            Icon(
                                Icons.Default.ModeComment,
                                "Comment icon"
                            )
                            Text(
                                "${image.comments}"
                            )
                        }


                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Not find image...",
                            Modifier.size(65.dp)
                        )
                    }
                }


            }
        }
    }
}
