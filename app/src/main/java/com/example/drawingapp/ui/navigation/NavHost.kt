package com.example.drawingapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.drawingapp.ui.home.HomeScreen
import com.example.drawingapp.ui.wordChain.screen.LoginScreen
import com.example.drawingapp.ui.wordChain.screen.PastGameScreen
import com.example.drawingapp.ui.wordChain.screen.WordChainMainScreen

@Composable
fun DisplayNav(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.AuthScreen.route,
        modifier = modifier
    ) {
        composable(route = Routes.Home.route) {
            HomeScreen(
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(route = Routes.AuthScreen.route) {
            LoginScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(route = Routes.WordChainMainScreen.route) {
            WordChainMainScreen(
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(
            route = Routes.PastGameScreen.route,
            arguments = listOf(
                navArgument("gameId") {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            val gameId = navBackStackEntry.arguments?.getString("gameId") ?: ""
            PastGameScreen(
                gameId = gameId,
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}