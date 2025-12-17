package com.chicken.tapbalance.navigation

sealed class AppDestination(val route: String) {
    data object Splash : AppDestination("splash")
    data object Menu : AppDestination("menu")
    data object Intro : AppDestination("intro")
    data object Game : AppDestination("game")
    data object Skins : AppDestination("skins")
}
