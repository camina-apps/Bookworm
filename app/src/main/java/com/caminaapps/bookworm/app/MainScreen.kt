package com.caminaapps.bookworm.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.caminaapps.bookworm.core.navigation.BookwormBottomNavigation
import com.caminaapps.bookworm.core.navigation.BookwormNavHost
import com.caminaapps.bookworm.core.navigation.Screen.Camera
import com.caminaapps.bookworm.core.navigation.Screen.SearchBookByTitle
import com.caminaapps.bookworm.core.navigation.Screen.SearchIsbnBookResult
import com.caminaapps.bookworm.core.ui.theme.BookwormTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
    }

    BookwormTheme {
        val navController = rememberNavController()
        var showBottomBar by rememberSaveable { mutableStateOf(true) }
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        showBottomBar = when (navBackStackEntry?.destination?.route) {
            SearchBookByTitle.route -> false
            SearchIsbnBookResult.route -> false
            Camera.route -> false
            else -> true
        }

        Scaffold(
            modifier = modifier,
            bottomBar = { if (showBottomBar) BookwormBottomNavigation(navController = navController) }
        ) { innerPadding ->
            BookwormNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@Preview(showBackground = true)
@Composable
fun DefaultPreviewMainScreen() {
    MainScreen()
}
