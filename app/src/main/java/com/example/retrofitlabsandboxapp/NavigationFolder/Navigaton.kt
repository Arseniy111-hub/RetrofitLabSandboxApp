package com.example.retrofitlabsandboxapp.NavigationFolder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.retrofitlabsandboxapp.Logs.Tags
import com.example.retrofitlabsandboxapp.Logs.writeLog
import com.example.retrofitlabsandboxapp.ScreensFolder.LogsScreen
import com.example.retrofitlabsandboxapp.ScreensFolder.PostScreens.FullScreenPost
import com.example.retrofitlabsandboxapp.ScreensFolder.PostScreens.Post
import com.example.retrofitlabsandboxapp.ScreensFolder.Weather
import com.example.retrofitlabsandboxapp.mvi.image.ImageViewModel
import com.example.retrofitlabsandboxapp.mvi.post.PostIntent
import com.example.retrofitlabsandboxapp.mvi.post.PostViewModel
import com.example.retrofitlabsandboxapp.mvi.weather.WeatherViewModel

@Composable
fun Navigation(
    postViewModel: PostViewModel,
    imageViewModel: ImageViewModel,
    weatherViewModel: WeatherViewModel,
    navController: NavHostController

) {
    val ps = postViewModel.postState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screens.Post.route,

        ) {
        composable(
            route = Screens.Post.route,

            ) {


            Post(
                onIntent = { intent -> postViewModel.onIntent(intent) },
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
                    postViewModel.onIntent(PostIntent.FetchPostOnId(postId))
                } else {
                    writeLog("Post id is null", Tags.NAVIGATION)
                }
            }

            FullScreenPost(
                postId = postId,
                onIntent = { intent -> postViewModel.onIntent(intent) },
                state = ps.value,
                onBack = {
                    navController.popBackStack()
                    postViewModel.onIntent(PostIntent.UpdateErrorMessage(""))
                }
            )
        }
        composable(
            route = Screens.Image.route
        ) {
            // Image()
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
                onIntent = { postViewModel.onIntent(it) },
                state = ps.value
            )
        }

    }
}
