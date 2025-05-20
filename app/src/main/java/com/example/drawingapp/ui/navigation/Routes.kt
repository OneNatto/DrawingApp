package com.example.drawingapp.ui.navigation

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object AuthScreen : Routes("auth_screen")
    object WaitingRoomScreen : Routes("waiting_room_screen")
    object WordChainMainScreen : Routes("word_chain_main_screen")
    object PastGameScreen : Routes("past_game_screen?gameId={gameId}")
}