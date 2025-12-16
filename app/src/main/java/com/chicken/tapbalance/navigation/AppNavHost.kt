package com.chicken.tapbalance.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chicken.tapbalance.ui.game.GameScreen
import com.chicken.tapbalance.ui.game.GameViewModel
import com.chicken.tapbalance.ui.menu.MenuScreen
import com.chicken.tapbalance.ui.menu.MenuViewModel
import com.chicken.tapbalance.ui.skins.SkinsScreen
import com.chicken.tapbalance.ui.skins.SkinsViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Menu.route,
        modifier = modifier
    ) {
        composable(AppDestination.Menu.route) {
            val viewModel: MenuViewModel = hiltViewModel()
            MenuScreen(
                state = viewModel.uiState,
                onPlay = { navController.navigate(AppDestination.Game.route) },
                onSkins = { navController.navigate(AppDestination.Skins.route) },
                onMusicToggle = viewModel::setMusicEnabled,
                onSfxToggle = viewModel::setSfxEnabled
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
