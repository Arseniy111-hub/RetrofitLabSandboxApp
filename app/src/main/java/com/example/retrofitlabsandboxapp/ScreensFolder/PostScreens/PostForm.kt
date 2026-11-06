package com.example.retrofitlabsandboxapp.ScreensFolder.PostScreens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent
import com.example.retrofitlabsandboxapp.mvi.post.PostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostForm(
    modifier: Modifier = Modifier,
    onIntent: (PostIntent) -> Unit,
    state: PostState
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = {
            onIntent(PostIntent.UpdatePostFormFlag(false))

        },
        modifier = modifier

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Icon(
                    Icons.Default.Title,
                    "Title input"
                )
                TextField(
                    value = state.userTitle,
                    onValueChange = { onIntent(PostIntent.UpdateUserTitle(it)) },
                    placeholder = { Text("title") },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                onIntent(PostIntent.UpdateUserTitle(""))
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                "Delete text"
                            )
                        }
                    }

                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Icon(
                    Icons.Default.ContentPaste,
                    "Title body"
                )
                TextField(
                    value = state.userBody,
                    onValueChange = { onIntent(PostIntent.UpdateUserBody(it)) },
                    placeholder = { Text("text") },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                onIntent(PostIntent.UpdateUserBody(""))
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                "Delete text"
                            )
                        }
                    }
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Icon(
                    Icons.Default.PermIdentity,
                    "Title input"
                )
                TextField(
                    value = state.userId,
                    onValueChange = { onIntent(PostIntent.UpdateUserId(it)) },
                    placeholder = { Text("your id") },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                onIntent(PostIntent.UpdateUserId(""))
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                "Delete text"
                            )
                        }
                    }
                )

            }
            var attemptsCount: Int by remember { mutableIntStateOf(0) }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    if (state.userBody.isNotBlank()
                        && state.userTitle.isNotBlank()
                        && state.userId.isNotBlank()
                    ) {
                        if (state.userId.toIntOrNull() in 1..10) {
                            onIntent(PostIntent.CreateUserPost)
                            onIntent(PostIntent.UpdatePostFormFlag(false))
                            onIntent(PostIntent.ClearUserForm)
                        } else {
                            ++attemptsCount
                            if (attemptsCount > 2) {
                                Toast.makeText(
                                    context,
                                    "user id must be digit in 1 <= .. <= 10",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(context, "Incorrect user id", Toast.LENGTH_SHORT)
                                    .show()

                            }

                        }

                    } else {
                        Toast.makeText(context, "Any params id is blank", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Save")
            }

        }
    }
}
