package com.caminaapps.bookworm.core.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.caminaapps.bookworm.core.presentation.navigation.BookwormBottomNavigation
import com.caminaapps.bookworm.core.presentation.navigation.BookwormNavHost
import com.caminaapps.bookworm.core.presentation.navigation.Screen
import com.caminaapps.bookworm.core.presentation.theme.BookwormTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    BookwormTheme {
        val navController = rememberNavController()
        var showBottomBar by rememberSaveable { mutableStateOf(true) }
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        showBottomBar = when (navBackStackEntry?.destination?.route) {
            Screen.SearchIsbnBookResult.route -> false
            Screen.Camera.route -> false
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