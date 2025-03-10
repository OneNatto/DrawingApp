package com.example.drawingapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
        startDestination = Routes.Home.route,
        modifier = modifier
    ) {
        composable(route = Routes.Home.route) {
            HomeScreen(
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(route = Routes.WordChainMainScreen.route) {
            WordChainMainScreen(
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}