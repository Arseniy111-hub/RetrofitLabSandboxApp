package com.example.retrofitlabsandboxapp.NavigationFolder

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@SuppressLint("UnrememberedMutableState")
@Composable
fun MyTabRow(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val startDestination = Destination.POST
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
    PrimaryTabRow(
        selectedTabIndex = selectedDestination,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.onPrimary,
        contentColor = MaterialTheme.colorScheme.primary,

        ) {
        Destination.entries.forEachIndexed { index, destination ->
            Tab(
                selected = selectedDestination == index,
                onClick = {
                    navController.navigate(destination.route)
                    selectedDestination = index
                },
                text = {
                    Text(destination.name)
                }
            )
        }
    }
}