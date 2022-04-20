package com.caminaapps.bookworm.presentation.screens.bookshelf.components


import android.Manifest
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.caminaapps.bookworm.R
import com.caminaapps.bookworm.presentation.components.CameraPermissionAlertDialog
import com.caminaapps.bookworm.presentation.components.SpeedDialFloatingActionButton
import com.caminaapps.bookworm.presentation.components.SpeedDialItem
import com.caminaapps.bookworm.presentation.theme.BookwormTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import timber.log.Timber


@ExperimentalPermissionsApi
@Composable
fun AddBookFloatingActionButton(
    onManual: () -> Unit,
    onScan: () -> Unit
) {
    SpeedDialFloatingActionButton(
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = stringResource(id = R.string.button_add)
            )
        }
    ) {
        SpeedDialItem(onClick = onManual) {
            Icon(
                painter = painterResource(id = R.drawable.ic_outline_edit_24),
                contentDescription = stringResource(id = R.string.button_edit)
            )
        }
        CameraSpeedDialItem(onScan = onScan)
    }
}

@ExperimentalPermissionsApi
@Composable
fun ColumnScope.CameraSpeedDialItem(onScan: () -> Unit) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var openDialog by remember { mutableStateOf(false) }

    SpeedDialItem(onClick = {
        if (cameraPermissionState.hasPermission) {
            Timber.d("start camera")
            onScan()
        } else if (!cameraPermissionState.permissionRequested) {
            cameraPermissionState.launchPermissionRequest()
        } else if (!cameraPermissionState.hasPermission) {
            openDialog = true
        }
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_outline_photo_camera_24),
            contentDescription = stringResource(id = R.string.button_scan)
        )
    }

    if (openDialog) {
        CameraPermissionAlertDialog(
            onDismissRequest = { openDialog = false },
            onConfirm = {},
            cameraPermissionState = cameraPermissionState
        )
    }
}


@ExperimentalPermissionsApi
@Preview(showBackground = true, name = "add floating button")
@Composable
fun PreviewAddFloatingButton() {
    BookwormTheme {
        Scaffold(
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                AddBookFloatingActionButton(
                    onManual = { /*TODO*/ },
                    onScan = {  /*TODO*/ }
                )
            }
        ) { }
    }
}