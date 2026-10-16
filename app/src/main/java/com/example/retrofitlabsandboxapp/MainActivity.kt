package com.example.retrofitlabsandboxapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.retrofitlabsandboxapp.NavigationFolder.MyTabRow
import com.example.retrofitlabsandboxapp.NavigationFolder.Navigation
import com.example.retrofitlabsandboxapp.mvi.image.ImageViewModel
import com.example.retrofitlabsandboxapp.mvi.post.PostViewModel
import com.example.retrofitlabsandboxapp.mvi.weather.WeatherViewModel
import com.example.retrofitlabsandboxapp.ui.theme.RetrofitLabSandboxAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pvm by viewModels<PostViewModel>()
        val ivm by viewModels<ImageViewModel>()
        val wim by viewModels<WeatherViewModel>()


        enableEdgeToEdge()
        setContent {
            RetrofitLabSandboxAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomAppBar(
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            MyTabRow(
                                modifier = Modifier.fillMaxHeight(),
                                navController = navController
                            )
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        Navigation(
                            pvm = pvm,
                            ivm = ivm,
                            wim = wim,
                            navController = navController
                        )

                    }
                }
            }
        }
    }
}

