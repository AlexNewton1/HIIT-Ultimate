package com.softwareoverflow.hiit_trainer.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.FolderCopy
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.result.EmptyResultRecipient
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.rememberAppState
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.ui.consent.UserConsentManager
import com.softwareoverflow.hiit_trainer.ui.destinations.LoadWorkoutScreenDestination
import com.softwareoverflow.hiit_trainer.ui.destinations.SettingsScreenDestination
import com.softwareoverflow.hiit_trainer.ui.destinations.UpgradeScreenDestination
import com.softwareoverflow.hiit_trainer.ui.destinations.UserConsentScreenDestination
import com.softwareoverflow.hiit_trainer.ui.destinations.WorkoutCreatorScreenDestination
import com.softwareoverflow.hiit_trainer.ui.navigation.NavigationResultActionBasic
import com.softwareoverflow.hiit_trainer.ui.settings.AppDrawer
import com.softwareoverflow.hiit_trainer.ui.theme.AppTheme
import com.softwareoverflow.hiit_trainer.ui.theme.spacing
import com.softwareoverflow.hiit_trainer.ui.upgrade.UpgradeManager
import com.softwareoverflow.hiit_trainer.ui.utils.compose.TopAppRow
import kotlinx.coroutines.launch

@Destination
@RootNavGraph(start = true)
@Composable
fun HomeScreen(navigator: DestinationsNavigator, resultRecipient: ResultRecipient<SettingsScreenDestination, NavigationResultActionBasic>) {

    val appState = rememberAppState()
    val drawerState = appState.scaffoldState.drawerState

    val toggleDrawer: () -> Unit = {
        appState.coroutineScope.launch {
            drawerState.apply {
                if (isClosed) open() else close()
            }
        }
    }

    BackHandler(enabled = drawerState.isOpen) {
        toggleDrawer()
    }

    Scaffold(scaffoldState = appState.scaffoldState,
        // TODO remove snackbar stuff from here - it's already in MainActivity!
        snackbarHost = {
            // reuse default SnackbarHost to have default animation and timing handling
            SnackbarHost(it) { data ->
                // custom snackbar with the custom colors
                Snackbar(
                    actionColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onBackground,
                    backgroundColor = Color.White,
                    snackbarData = data
                )
            }
        },
        drawerContent = {
        AppDrawer(openSettings = {
            toggleDrawer()
            navigator.navigate(SettingsScreenDestination)
        })
    }, drawerGesturesEnabled = false, topBar = {
        TopAppRow(
            startIcon = Icons.Filled.Menu, onStartPressed = {
                toggleDrawer()
            }, title = stringResource(id = R.string.app_name), endIcon = null
        )
    }) { padding ->
            Column(
                Modifier.fillMaxSize().padding(padding), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.size(MaterialTheme.spacing.extraExtraExtraLarge))

                HomeScreenRow(icon = Icons.Filled.AddCircle,
                    stringId = R.string.create_new_workout,
                    onClick = {
                        navigator.navigate(WorkoutCreatorScreenDestination(WorkoutDTO()))
                    })

                HomeScreenRow(icon = Icons.Filled.FolderCopy,
                    stringId = R.string.load_saved_workout,
                    onClick = {
                        navigator.navigate(LoadWorkoutScreenDestination)
                    })

                Spacer(Modifier.weight(1f))

                if (!UpgradeManager.isUserUpgraded()) {
                    HomeScreenRow(
                        icon = Icons.Filled.Upgrade,
                        stringId = R.string.upgrade_to_pro,
                        onClick = {
                            navigator.navigate(UpgradeScreenDestination)
                        })
                }

                Spacer(Modifier.size(MaterialTheme.spacing.extraExtraExtraLarge))
            }

        if (!UserConsentManager.consentEverGiven) {
            navigator.navigate(UserConsentScreenDestination)
        }
    }

    val settingsUpdateMessage = stringResource(id = R.string.settings_saved)
    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {/* Do Nothing */
            }

            is NavResult.Value -> {
                if (result.value == NavigationResultActionBasic.ACTION_POSITIVE)
                    SnackbarManager.showMessage(settingsUpdateMessage)
            }
        }
    }
}

@Composable
private fun HomeScreenRow(icon: ImageVector, @StringRes stringId: Int, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(0.8f), horizontalArrangement = Arrangement.Center) {
        Button(onClick = onClick, Modifier.padding(vertical = MaterialTheme.spacing.medium)) {
            Row(horizontalArrangement = Arrangement.SpaceAround) {
                Icon(icon, null)
                Text(
                    stringResource(id = stringId),
                    Modifier.padding(horizontal = MaterialTheme.spacing.small)
                )
            }
        }
    }
}

@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Preview(name = "Phone", device = Devices.PIXEL_4_XL)
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(EmptyDestinationsNavigator, EmptyResultRecipient())
    }
}