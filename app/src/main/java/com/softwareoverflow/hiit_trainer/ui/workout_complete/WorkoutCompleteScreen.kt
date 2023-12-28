package com.softwareoverflow.hiit_trainer.ui.workout_complete

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.ui.destinations.HomeScreenDestination
import com.softwareoverflow.hiit_trainer.ui.destinations.UnsavedChangesWarningScreenDestination
import com.softwareoverflow.hiit_trainer.ui.destinations.UpgradeScreenDestination
import com.softwareoverflow.hiit_trainer.ui.destinations.WorkoutSaverScreenDestination
import com.softwareoverflow.hiit_trainer.ui.navigation.NavigationResultActionBasic
import com.softwareoverflow.hiit_trainer.ui.theme.AppTheme
import com.softwareoverflow.hiit_trainer.ui.theme.spacing
import com.softwareoverflow.hiit_trainer.ui.upgrade.UpgradeManager
import com.softwareoverflow.hiit_trainer.ui.utils.compose.AppScreen
import com.softwareoverflow.hiit_trainer.ui.utils.compose.TopAppRow
import com.softwareoverflow.hiit_trainer.ui.utils.compose.findActivity

@Destination
@Composable
fun WorkoutCompleteScreen(
    workout: WorkoutDTO,
    viewModel: WorkoutCompleteViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    unsavedChangesResult: ResultRecipient<UnsavedChangesWarningScreenDestination, NavigationResultActionBasic>
) {

    val context = LocalContext.current
    val activity = context.findActivity()
    LaunchedEffect(workout) {
        viewModel.initialize(context, activity)
    }

    BackHandler(enabled = true) {
        if(viewModel.showUnsavedChangesWarning){
            navigator.navigate(UnsavedChangesWarningScreenDestination)
        }
        else {
            navigator.popBackStack(HomeScreenDestination, inclusive = false)
            navigator.clearBackStack(HomeScreenDestination)
        }
    }

    unsavedChangesResult.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> { /* Do Nothing */
            }

            is NavResult.Value -> {
                if (result.value == NavigationResultActionBasic.ACTION_POSITIVE) navigator.popBackStack(
                    HomeScreenDestination, false
                )
            }
        }
    }

    AppScreen(topAppRow = {
        TopAppRow(startIcon = {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = Icons.Filled.ArrowBack.name,
                Modifier.clickable {
                    if (viewModel.showUnsavedChangesWarning) navigator.navigate(
                        UnsavedChangesWarningScreenDestination
                    )
                    else navigator.popBackStack(HomeScreenDestination, false)
                },
                tint = MaterialTheme.colors.onPrimary
            )
        }, title = stringResource(id = R.string.workout_complete), endIcon = {
            Icon(
                painterResource(id = R.drawable.icon_save_options),
                contentDescription = stringResource(
                    id = R.string.save
                ),
                Modifier.clickable {
                    navigator.navigate(WorkoutSaverScreenDestination(workout))
                },
                tint = MaterialTheme.colors.onPrimary
            )
        })
    }, bottomAppRow = null) { modifier ->
        Content(modifier = modifier, onUpgrade = {
            navigator.navigate(UpgradeScreenDestination)
        }, onHomePress = {
            if (viewModel.showUnsavedChangesWarning) navigator.navigate(
                UnsavedChangesWarningScreenDestination
            )
            else navigator.popBackStack(HomeScreenDestination, false)

        })
    }
}

@Composable
private fun Content(modifier: Modifier, onUpgrade: () -> Unit, onHomePress: () -> Unit) {

    Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.icon_trophy),
            contentDescription = stringResource(
                id = R.string.workout_complete
            ),
            Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(1f)
                .background(MaterialTheme.colors.primary, CircleShape)
                .padding(MaterialTheme.spacing.large),
            tint = MaterialTheme.colors.onPrimary
        )

        Text(stringResource(id = R.string.workout_complete), style = MaterialTheme.typography.h4)

        Spacer(modifier = Modifier.weight(1f))

        if (!UpgradeManager.isUserUpgraded()) {
            Row(Modifier.fillMaxWidth(0.8f), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = onUpgrade, Modifier.padding(vertical = MaterialTheme.spacing.medium)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Upgrade, null)
                        Text(
                            stringResource(id = R.string.upgrade_to_pro),
                            Modifier.padding(horizontal = MaterialTheme.spacing.small)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        FloatingActionButton(onClick = { onHomePress() }, Modifier.align(Alignment.End)) {
            Icon(Icons.Filled.Home, contentDescription = Icons.Filled.Home.name)
        }
    }

}

@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Preview(name = "Phone", device = Devices.PIXEL_4_XL)
@Composable
private fun Preview() {
    AppTheme {
        Content(modifier = Modifier, onUpgrade = {}, onHomePress = {})
    }
}