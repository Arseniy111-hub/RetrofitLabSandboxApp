package com.example.retrofitlabsandboxapp.NavigationFolder

enum class Destination(
    val route: String
) {
    POST(route = Screens.Post.route),
    IMAGE(route = Screens.Image.route),
    WEATHER(route = Screens.Weather.route),
    LOGS(route = Screens.LogsScreen.route)
}