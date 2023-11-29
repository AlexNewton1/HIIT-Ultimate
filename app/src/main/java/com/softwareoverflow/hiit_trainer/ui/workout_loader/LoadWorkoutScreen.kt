package com.softwareoverflow.hiit_trainer.ui.workout_loader

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.destinations.WorkoutCreatorScreenDestination
import com.softwareoverflow.hiit_trainer.ui.destinations.WorkoutScreenDestination
import com.softwareoverflow.hiit_trainer.ui.getFormattedDuration
import com.softwareoverflow.hiit_trainer.ui.theme.AppTheme
import com.softwareoverflow.hiit_trainer.ui.theme.spacing
import com.softwareoverflow.hiit_trainer.ui.upgrade.MobileAdsManager
import com.softwareoverflow.hiit_trainer.ui.utils.compose.AppScreen
import com.softwareoverflow.hiit_trainer.ui.utils.compose.DropDownAction
import com.softwareoverflow.hiit_trainer.ui.utils.compose.DropDownItem
import com.softwareoverflow.hiit_trainer.ui.utils.compose.TopAppRow
import com.softwareoverflow.hiit_trainer.ui.utils.compose.findActivity
import com.softwareoverflow.hiit_trainer.ui.utils.compose.getDrawableId
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout.WorkoutLoaderDomainObject
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout.WorkoutLoaderDomainObjectType
import android.graphics.Color as GraphicsColor

@Composable
@Destination
fun LoadWorkoutScreen(
    navigator: DestinationsNavigator, viewModel: WorkoutLoaderViewModel = hiltViewModel()
) {

    val workouts = viewModel.workouts.observeAsState()

    val context = LocalContext.current


    AppScreen(topAppRow = {
        TopAppRow(startIcon = Icons.Filled.ArrowBack, onStartPressed = {
            navigator.popBackStack()
        }, title = stringResource(id = R.string.load_saved_workout))
    }, bottomAppRow = null, showBannerAd = true) { modifier ->
        LoadWorkoutScreenContent(
            modifier = modifier,
            workouts.value ?: emptyList(),
            onAction = { action, id ->
                workouts.value?.first { it.dto.id == id }?.let { workout ->
                    when (action) {
                        DropDownAction.START -> {
                            MobileAdsManager.showAdBeforeWorkout(
                                context.findActivity(),
                                onAdClosedCallback = {
                                    navigator.navigate(WorkoutScreenDestination(workout.dto))
                                })
                        }

                        DropDownAction.EDIT -> navigator.navigate(
                            WorkoutCreatorScreenDestination(
                                workout.dto
                            )
                        )

                        DropDownAction.DELETE -> viewModel.deleteWorkout(id)
                        else -> {/* Do Nothing */
                        }
                    }
                }
            })
    }
}

@Composable
private fun LoadWorkoutScreenContent(
    modifier: Modifier,
    workouts: List<WorkoutLoaderDomainObject>,
    onAction: (DropDownAction, Long) -> Unit
) {
    Column(modifier.fillMaxWidth()) {
        LazyColumn {
            items(workouts, key = { it.dto.id!! }) { workout ->

                if (workout.type == WorkoutLoaderDomainObjectType.USER) {

                    var isContentMenuVisible by remember { mutableStateOf(false) }
                    var pressOffset by remember {
                        mutableStateOf(DpOffset.Zero)
                    }
                    var itemHeight by remember {
                        mutableStateOf(0.dp)
                    }
                    val interactionSource = remember {
                        MutableInteractionSource()
                    }
                    val density = LocalDensity.current

                    Row(Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.extraSmall)
                        .background(
                            MaterialTheme.colors.primary.copy(alpha = 0.2f),
                            RoundedCornerShape(MaterialTheme.spacing.extraLarge / 2f)
                        )
                        .height(MaterialTheme.spacing.extraLarge)
                        .indication(interactionSource, LocalIndication.current)
                        .onSizeChanged {
                            with(density) {
                                itemHeight = it.height.toDp()
                            }
                        }
                        .pointerInput(true) {
                            detectTapGestures(onLongPress = {
                                isContentMenuVisible = true
                                pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                            }, onPress = {
                                val press = PressInteraction.Press(it)
                                interactionSource.emit(press)
                                tryAwaitRelease()
                                interactionSource.emit(PressInteraction.Release(press))
                            }, onTap = {
                                onAction(DropDownAction.EDIT, workout.dto.id!!)
                            })
                        }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_heart_pulse),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                .background(
                                    MaterialTheme.colors.primary, shape = CircleShape
                                )
                                .padding(MaterialTheme.spacing.extraSmall),
                            tint = MaterialTheme.colors.onPrimary
                        )

                        Column(
                            Modifier
                                .weight(1f)
                                .padding(vertical = MaterialTheme.spacing.extraExtraSmall)
                                .fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                workout.dto.name,
                                Modifier.weight(1f),
                                style = MaterialTheme.typography.body2
                            )
                            LazyRow(Modifier.weight(1f)) {
                                items(workout.dto.workoutSets) { workoutSet ->
                                    workoutSet.exerciseTypeDTO?.iconName?.let { iconName ->
                                        val id = getDrawableId(iconName, LocalContext.current)
                                        val bgColor =
                                            Color(GraphicsColor.parseColor(workoutSet.exerciseTypeDTO!!.colorHex))
                                        Icon(
                                            painterResource(id = id),
                                            contentDescription = null,
                                            Modifier
                                                .padding(MaterialTheme.spacing.extraExtraSmall)
                                                .fillMaxHeight()
                                                .aspectRatio(
                                                    1f, matchHeightConstraintsFirst = true
                                                )
                                                .background(
                                                    color = bgColor, shape = CircleShape
                                                )
                                                .padding(MaterialTheme.spacing.extraExtraSmall),
                                            tint = MaterialTheme.colors.onPrimary
                                        )
                                    }
                                }
                            }
                        }

                        Text(workout.dto.getFormattedDuration())

                        Icon(Icons.Filled.PlayArrow,
                            contentDescription = stringResource(id = R.string.start_workout),
                            Modifier
                                .clickable {
                                    onAction(DropDownAction.START, workout.dto.id!!)
                                }
                                .padding(horizontal = MaterialTheme.spacing.extraSmall)
                                .size(MaterialTheme.spacing.large),
                            tint = MaterialTheme.colors.onPrimary)

                        val dropDownItems = mutableListOf(
                            DropDownItem(R.string.start_workout, DropDownAction.START),
                            DropDownItem(R.string.edit, DropDownAction.EDIT),
                            DropDownItem(R.string.delete, DropDownAction.DELETE)
                        )

                        DropdownMenu(
                            expanded = isContentMenuVisible,
                            onDismissRequest = { isContentMenuVisible = false },
                            offset = pressOffset.copy(
                                x = pressOffset.x, y = pressOffset.y - itemHeight
                            )
                        ) {
                            dropDownItems.forEach { item ->

                                DropdownMenuItem(onClick = {
                                    isContentMenuVisible = false
                                    onAction(item.action, workout.dto.id!!)
                                }) {

                                    val icon = when (item.action) {
                                        DropDownAction.START -> Icons.Filled.PlayArrow
                                        DropDownAction.EDIT -> Icons.Filled.Edit
                                        DropDownAction.DELETE -> Icons.Filled.Delete
                                        else -> null
                                    }

                                    icon?.let {
                                        Icon(
                                            icon,
                                            contentDescription = icon.name,
                                            Modifier
                                                .size(MaterialTheme.spacing.large)
                                                .padding(MaterialTheme.spacing.extraExtraSmall)
                                        )
                                        Text(stringResource(id = item.text))
                                    }
                                }

                            }
                        }
                    }

                } else {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = MaterialTheme.spacing.extraSmall)
                            .background(
                                MaterialTheme.colors.primary.copy(alpha = 0.2f),
                                RoundedCornerShape(MaterialTheme.spacing.extraLarge / 2f)
                            ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (workout.type == WorkoutLoaderDomainObjectType.PLACEHOLDER_LOCKED) Icons.Filled.Lock else Icons.Filled.LockOpen,
                            contentDescription = null,
                            modifier = Modifier
                                .size(MaterialTheme.spacing.extraLarge)
                                .background(
                                    MaterialTheme.colors.primary, shape = CircleShape
                                )
                                .padding(MaterialTheme.spacing.extraSmall),
                            tint = Color.White
                        )

                        Text(
                            workout.dto.name,
                            Modifier.padding(MaterialTheme.spacing.small),
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Preview(name = "Phone", device = Devices.PIXEL_4_XL)
@Composable
private fun LoadWorkoutPreview() {
    val list = listOf(
        WorkoutLoaderDomainObject(
            WorkoutDTO(
                1, "Example Workout Name", workoutSets = mutableListOf(
                    WorkoutSetDTO(
                        exerciseTypeDTO = ExerciseTypeDTO(
                            1, "Any", "et_icon_run", "#FFFFC02B"
                        ), orderInWorkout = 0
                    ), WorkoutSetDTO(
                        exerciseTypeDTO = ExerciseTypeDTO(
                            2, "Something", "et_icon_flash", "#FF3F51B5"
                        )
                    )
                ), numReps = 3, recoveryTime = 30
            )
        ),
        WorkoutLoaderDomainObject(
            WorkoutDTO(
                2, "Another Workout", workoutSets = mutableListOf(
                    WorkoutSetDTO(
                        exerciseTypeDTO = ExerciseTypeDTO(
                            1, "Any", "et_icon_plank", "#FFFFC02B"
                        ), orderInWorkout = 0
                    ), WorkoutSetDTO(
                        exerciseTypeDTO = ExerciseTypeDTO(
                            2, "Something", "et_icon_dumbbell", "#FF3F51B5"
                        )
                    )
                ), numReps = 1, recoveryTime = 1000
            )
        ),

        WorkoutLoaderDomainObject.getPlaceholderUnlocked(LocalContext.current),
        WorkoutLoaderDomainObject.getPlaceholderLocked(LocalContext.current)
    )

    AppTheme {
        LoadWorkoutScreenContent(modifier = Modifier, workouts = list, onAction = { _, _ -> })
    }
}