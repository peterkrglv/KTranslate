package com.example.ktranslate.navigation

import android.util.Log
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ktranslate.icons.ArrowBackIcon
import com.example.ktranslate.icons.StarFilledIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(navController: NavController, currentRoute: String? = null) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Log.d("TopNavBar", "Current route: $currentRoute")
    Log.d("TopNavBar", "Back stack entry: ${navBackStackEntry}")
    CenterAlignedTopAppBar(title = { Text("KTranslate") }, navigationIcon = {
        IconButton(
            onClick = {
                when (currentRoute) {
                    Screen.Translate.route -> {
                        Log.d("TopNavBar", "Navigating to Favourites")
                        navController.navigate(Screen.Favourites.route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                                inclusive = false
                            }
                            restoreState = true
                        }
                    }

                    Screen.Favourites.route -> {
                        Log.d("TopNavBar", "Navigating to Translate")
                        navController.navigate(Screen.Translate.route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                                inclusive = false
                            }
                            restoreState = true
                        }
                    }
                }
            }) {
            Icon(
                imageVector = if (currentRoute == Screen.Translate.route) StarFilledIcon else ArrowBackIcon,
                contentDescription = if (currentRoute == Screen.Translate.route) "Star Favourites Icon" else "Back arrow Icon"
            )
        }
    })
}