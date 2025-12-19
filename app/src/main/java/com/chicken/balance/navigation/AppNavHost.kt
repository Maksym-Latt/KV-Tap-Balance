package com.chicken.balance.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chicken.balance.ui.game.GameScreen
import com.chicken.balance.ui.game.GameViewModel
import com.chicken.balance.ui.intro.IntroScreen
import com.chicken.balance.ui.menu.MenuScreen
import com.chicken.balance.ui.menu.MenuViewModel
import com.chicken.balance.ui.skins.SkinsScreen
import com.chicken.balance.ui.skins.SkinsViewModel
import com.chicken.balance.ui.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Splash.route,
        modifier = modifier
    ) {
        composable(AppDestination.Splash.route) {
            SplashScreen(
                onFinished = {
                    navController.navigate(AppDestination.Menu.route) {
                        popUpTo(AppDestination.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(AppDestination.Menu.route) {
            val viewModel: MenuViewModel = hiltViewModel()
            MenuScreen(
                state = viewModel.uiState,
                onPlay = { navController.navigate(AppDestination.Intro.route) },
                onSkins = { navController.navigate(AppDestination.Skins.route) },
                onMusicToggle = viewModel::setMusicEnabled,
                onSfxToggle = viewModel::setSfxEnabled
            )
        }
        composable(AppDestination.Intro.route) {
            IntroScreen(
                onStart = {
                    navController.navigate(AppDestination.Game.route) {
                        popUpTo(AppDestination.Menu.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(AppDestination.Game.route) {
            val viewModel: GameViewModel = hiltViewModel()
            GameScreen(
                viewModel = viewModel,
                onBackToMenu = {
                    viewModel.startIdle()
                    navController.popBackStack()
                }
            )
        }
        composable(AppDestination.Skins.route) {
            val viewModel: SkinsViewModel = hiltViewModel()
            SkinsScreen(
                state = viewModel.uiState,
                onBack = { navController.popBackStack() },
                onPurchase = viewModel::onPurchaseSkin,
                onSelect = viewModel::onSelectSkin,
                onReady = viewModel::onScreenShown
            )
        }
    }
}
