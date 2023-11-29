package com.softwareoverflow.hiit_trainer.ui.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.destinations.WorkoutCompleteScreenDestination
import com.softwareoverflow.hiit_trainer.ui.theme.AppTheme
import com.softwareoverflow.hiit_trainer.ui.theme.colorRecover
import com.softwareoverflow.hiit_trainer.ui.theme.colorRepeat
import com.softwareoverflow.hiit_trainer.ui.theme.colorRest
import com.softwareoverflow.hiit_trainer.ui.theme.colorWork
import com.softwareoverflow.hiit_trainer.ui.theme.spacing
import com.softwareoverflow.hiit_trainer.ui.utils.compose.AppScreen
import com.softwareoverflow.hiit_trainer.ui.utils.compose.BottomAppRow
import com.softwareoverflow.hiit_trainer.ui.utils.compose.KeepScreenOn
import com.softwareoverflow.hiit_trainer.ui.utils.compose.TopAppRow
import com.softwareoverflow.hiit_trainer.ui.utils.compose.getDrawableId
import android.graphics.Color as GraphicsColor


@Destination
@Composable
fun WorkoutScreen(
    workout: WorkoutDTO, viewModel: WorkoutViewModel = viewModel(
        factory = WorkoutViewModelFactory(
            LocalContext.current, workout
        )
    ), navigator: DestinationsNavigator
) {

    val isWorkoutFinished by viewModel.isWorkoutFinished.collectAsState()
    val uiState = viewModel.uiState.collectAsState()

    if (isWorkoutFinished) {
        navigator.navigate(WorkoutCompleteScreenDestination(workout))
    }

    KeepScreenOn()

    AppScreen(topAppRow = {
        TopAppRow(
            startIcon = Icons.Filled.ArrowBack,
            onStartPressed = { navigator.popBackStack() },
            title = stringResource(id = R.string.nav_workout)
        )
    }, bottomAppRow = {
        BottomAppRow {
            val soundIcon =
                if (uiState.value.isSoundOn) Icons.Filled.VolumeUp else Icons.Filled.VolumeOff
            val pauseIcon =
                if (uiState.value.isPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause

            Icon(soundIcon, contentDescription = soundIcon.name,
                Modifier
                    .weight(1f)
                    .clickable {
                        viewModel.toggleSound()
                    })
            Icon(pauseIcon, contentDescription = pauseIcon.name,
                Modifier
                    .weight(1f)
                    .clickable {
                        viewModel.togglePause()
                    })
            Icon(Icons.Filled.FastForward,
                contentDescription = Icons.Filled.SkipNext.name,
                Modifier
                    .weight(1f)
                    .clickable {
                        viewModel.skipSection()
                    })
        }
    }, showBannerAd = true) { modifier ->
        Content(
            modifier = modifier,
            uiState.value,
        )
    }

}

@Composable
private fun Content(
    modifier: Modifier,
    uiState: WorkoutViewModel.UiState,
) {

    Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val iconId = getDrawableId(uiState.currentExerciseType.iconName!!, LocalContext.current)
            Icon(
                painterResource(id = iconId),
                contentDescription = uiState.currentExerciseType.name,
                Modifier
                    .size(MaterialTheme.spacing.extraExtraLarge)
                    .background(
                        Color(GraphicsColor.parseColor(uiState.currentExerciseType.colorHex!!)),
                        CircleShape
                    )
                    .padding(MaterialTheme.spacing.small),
                tint = MaterialTheme.colors.onPrimary
            )
            Text(
                text = uiState.currentExerciseType.name!!,
                Modifier
                    .padding(MaterialTheme.spacing.small)
                    .weight(1f)
            )

            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_repeat),
                    contentDescription = stringResource(
                        id = R.string.num_reps
                    ),
                    tint = colorRepeat,
                    modifier = Modifier.size(MaterialTheme.spacing.extraExtraLarge)
                )

                Text(uiState.currentRep)
            }
        }

        Box(
            Modifier
                .height(MaterialTheme.spacing.extraExtraLarge)
                .fillMaxWidth()
        ) {

            Column(
                Modifier
                    .height(MaterialTheme.spacing.extraExtraLarge)
                    .fillMaxWidth()
            ) {
                AnimatedVisibility(visible = uiState.upNextExerciseType != null) {
                    Column(

                    ) {
                        uiState.upNextExerciseType?.let { upNextExerciseType ->
                            Text(stringResource(id = R.string.up_next))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painterResource(
                                        id = getDrawableId(
                                            upNextExerciseType.iconName!!, LocalContext.current
                                        )
                                    ),
                                    contentDescription = stringResource(
                                        id = R.string.up_next
                                    ),
                                    Modifier
                                        .fillMaxHeight(1f)
                                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                        .background(
                                            Color(GraphicsColor.parseColor(upNextExerciseType.colorHex!!)),
                                            CircleShape
                                        )
                                        .padding(MaterialTheme.spacing.small),
                                    tint = MaterialTheme.colors.onPrimary
                                )

                                Text(
                                    upNextExerciseType.name!!,
                                    Modifier.padding(MaterialTheme.spacing.extraSmall)
                                )
                            }
                        }
                    }
                }
            }
        }

        Text(uiState.sectionTimeRemaining, style = MaterialTheme.typography.h1)

        val sectionIcon = when (uiState.currentSection) {
            WorkoutSection.PREPARE -> R.drawable.icon_heart_pulse
            WorkoutSection.WORK -> R.drawable.icon_fire
            WorkoutSection.REST -> R.drawable.icon_rest
            WorkoutSection.RECOVER -> R.drawable.icon_recover
        }
        val sectionIconTint = when (uiState.currentSection) {
            WorkoutSection.PREPARE -> MaterialTheme.colors.onPrimary
            WorkoutSection.WORK -> colorWork
            WorkoutSection.REST -> colorRest
            WorkoutSection.RECOVER -> colorRecover
        }

        Icon(
            painterResource(id = sectionIcon),
            contentDescription = stringResource(id = R.string.content_desc_workout_section_icon),
            Modifier
                .weight(1f)
                .aspectRatio(1f, matchHeightConstraintsFirst = true),
            tint = sectionIconTint
        )
        Text(
            uiState.currentSection.name,
            color = sectionIconTint,
            style = MaterialTheme.typography.h3
        )

        Text(
            text = "${stringResource(id = R.string.remaining_time)} ${uiState.workoutTimeRemaining}",
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
        )
    }

}

@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Preview(name = "Phone", device = Devices.PIXEL_4_XL)
@Composable
private fun Preview() {
    AppTheme {
        Content(
            modifier = Modifier,

            WorkoutViewModel.UiState(
                currentWorkoutSet = WorkoutSetDTO(
                    ExerciseTypeDTO(
                        1L, "Bicep Curls", "et_icon_dumbbell", colorHex = "#FFFD5C29"
                    )
                ),
                upNextExerciseType = null,
                currentSection = WorkoutSection.RECOVER,
                sectionTimeRemainingValue = 7,
                workoutTimeRemainingValue = 156,
                currentRepValue = 3,

                isPaused = false,
                isSoundOn = true
            )
        )
    }
}