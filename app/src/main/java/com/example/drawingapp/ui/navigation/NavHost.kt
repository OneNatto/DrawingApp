package com.example.drawingapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.drawingapp.ui.home.HomeScreen
import com.example.drawingapp.ui.wordChain.screen.AuthScreen
import com.example.drawingapp.ui.wordChain.screen.PastGameScreen
import com.example.drawingapp.ui.wordChain.screen.WaitingRoomScreen
import com.example.drawingapp.ui.wordChain.screen.WordChainMainScreen

@Composable
fun DisplayNav(
    viewModel: NavHostViewModel = viewModel(),
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val startDestination = if(isLoggedIn) Routes.Home.route else Routes.AuthScreen.route

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.AuthScreen.route) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Routes.AuthScreen.route) {
                popUpTo(Routes.Home.route) {
                    inclusive = true
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Routes.Home.route) {
            HomeScreen(
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(route = Routes.AuthScreen.route) {
            AuthScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(route = Routes.WaitingRoomScreen.route) {
            WaitingRoomScreen(
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