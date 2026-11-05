package com.example.retrofitlabsandboxapp.NavigationFolder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.retrofitlabsandboxapp.ScreensFolder.Image
import com.example.retrofitlabsandboxapp.ScreensFolder.LogsScreen
import com.example.retrofitlabsandboxapp.ScreensFolder.PostScreens.FullScreenPost
import com.example.retrofitlabsandboxapp.ScreensFolder.PostScreens.Post
import com.example.retrofitlabsandboxapp.ScreensFolder.Weather
import com.example.retrofitlabsandboxapp.Tags
import com.example.retrofitlabsandboxapp.mvi.image.ImageViewModel
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent
import com.example.retrofitlabsandboxapp.mvi.post.PostViewModel
import com.example.retrofitlabsandboxapp.mvi.weather.WeatherViewModel
import com.example.retrofitlabsandboxapp.writeLog

@Composable
fun Navigation(
    pvm: PostViewModel,
    ivm: ImageViewModel,
    wim: WeatherViewModel,
    navController: NavHostController

) {
    val ps = pvm.postState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screens.Post.route,

        ) {
        composable(
            route = Screens.Post.route,

            ) {


            Post(
                onIntent = { intent -> pvm.onIntent(intent) },
                state = ps.value,
                onBack = { navController.popBackStack() },
                onFullScreen = { id ->
                    navController.navigate(
                        Screens.FullScreenPost.postRoute(id)
                    )
                }
            )
        }
        composable(
            route = Screens.FullScreenPost.route,
            arguments = listOf(
                navArgument(
                    name = "postId"
                ) {
                    type = NavType.IntType
                }
            )
        ) {
            val postId = it.arguments?.getInt("postId")
            LaunchedEffect(postId) {
                if (postId != null) {
                    pvm.onIntent(PostIntent.FetchPostOnId(postId))
                } else {
                    writeLog("Post id is null", Tags.NAVIGATION)
                }
            }

            FullScreenPost(
                onIntent = { intent -> pvm.onIntent(intent) },
                state = ps.value,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screens.Image.route
        ) {
            Image()
        }
        composable(
            route = Screens.Weather.route
        ) {
            Weather()
        }
        composable(
            route = Screens.LogsScreen.route
        ) {
            LogsScreen(
                onBack = { navController.popBackStack() },
                onIntent = { pvm.onIntent(it) },
                state = ps.value
            )
        }

    }
}
