package com.example.retrofitlabsandboxapp.NavigationFolder

sealed class Screens(val route: String) {
    object Post : Screens("post")
    object Image : Screens("image/{uri}/{imageId}") {
        fun imageRoute(uri: String, imageId: Int) = "image/$uri/$imageId"
    }

    object Weather : Screens("weather") {
        //Todo("Добавить параметры, передаваемые через путь...")
    }

    object FullScreenPost : Screens("postFullScreen/{postId}") {
        fun postRoute(postId: Int) = "postFullScreen/$postId"
    }
    object LogsScreen : Screens("logsScreen")

}
