package com.caminaapps.bookworm.presentation.screens.bookshelf

import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.caminaapps.bookworm.presentation.screens.bookshelf.components.AddBookFloatingActionButton
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@Composable
fun BookshelfScreen(
    viewModel: BookshelfViewModel
) {
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {

            AddBookFloatingActionButton(
                onManual = { /*TODO*/ },
                onScan = { }
            )
        }
    ) {

        Text("- Bookshelf -")

    }
}




