package com.example.drawingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.drawingapp.ui.home.HomeScreen
import com.example.drawingapp.ui.wordChain.WordChainMainScreen

@Composable
fun DisplayNav(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "",
        modifier = modifier
    ) {
        composable(route = Routes.Home.route) {
            HomeScreen()
        }
        composable(route = Routes.WordChainMainScreen.route) {
            WordChainMainScreen()
        }
    }
}