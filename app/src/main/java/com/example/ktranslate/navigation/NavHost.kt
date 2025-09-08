package com.example.ktranslate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ktranslate.favourites_screen.FavouritesView
import com.example.ktranslate.translate_screen.TranslateView

sealed class Screen(val route: String) {
    object Translate : Screen("translate")
    object Favourites : Screen("favourites")
}

@Composable
fun SetUpNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Translate.route
    ) {
        composable(route = Screen.Translate.route) {
            TranslateView(
            )
        }
        composable(route = Screen.Favourites.route) {
            FavouritesView(
            )
        }
    }
}