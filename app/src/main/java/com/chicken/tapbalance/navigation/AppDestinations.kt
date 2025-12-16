package com.chicken.tapbalance.navigation

sealed class AppDestination(val route: String) {
    data object Menu : AppDestination("menu")
    data object Game : AppDestination("game")
    data object Skins : AppDestination("skins")
}
