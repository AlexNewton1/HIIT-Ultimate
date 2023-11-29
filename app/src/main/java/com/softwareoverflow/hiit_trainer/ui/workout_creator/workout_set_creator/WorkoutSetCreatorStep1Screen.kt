package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.SnackbarManager
import com.softwareoverflow.hiit_trainer.ui.destinations.ExerciseTypeCreatorScreenDestination
import com.softwareoverflow.hiit_trainer.ui.destinations.WorkoutSetCreatorStep2Destination
import com.softwareoverflow.hiit_trainer.ui.theme.AppTheme
import com.softwareoverflow.hiit_trainer.ui.theme.spacing
import com.softwareoverflow.hiit_trainer.ui.utils.compose.AppScreen
import com.softwareoverflow.hiit_trainer.ui.utils.compose.BottomAppRow
import com.softwareoverflow.hiit_trainer.ui.utils.compose.DropDownAction
import com.softwareoverflow.hiit_trainer.ui.utils.compose.DropDownItem
import com.softwareoverflow.hiit_trainer.ui.utils.compose.TopAppRow
import com.softwareoverflow.hiit_trainer.ui.utils.compose.getDrawableId

@Destination
@Composable
fun WorkoutSetCreatorStep1(
    workoutDTO: WorkoutDTO,
    workoutSetDTO: WorkoutSetDTO,
    viewModel: WorkoutSetCreatorViewModel = viewModel(
        factory = WorkoutSetCreatorViewModelFactory(
            workoutSetDTO, LocalContext.current
        )
    ),
    navigator: DestinationsNavigator,
    etResultRecipient: ResultRecipient<ExerciseTypeCreatorScreenDestination, Long>,
    resultBackNavigator: ResultBackNavigator<WorkoutSetDTO>,
    resultRecipient: ResultRecipient<WorkoutSetCreatorStep2Destination, WorkoutSetDTO> // TODO FUTURE VERSION can I just put this in the previous step, and then popBackStack from step2?
) {

    val exerciseTypes = viewModel.allExerciseTypes.observeAsState()

    val selectedId = viewModel.selectedExerciseTypeId.observeAsState()

    val screenTitle = if (workoutSetDTO.orderInWorkout == null) stringResource(
        id = R.string.nav_set_creator_step_1
    ) else stringResource(
        id = R.string.nav_set_editor_step_1
    )

    val snackbarMessage by viewModel.unableToDeleteExerciseType.observeAsState()
    if(!snackbarMessage.isNullOrBlank()){
        SnackbarManager.showMessage(snackbarMessage.toString())
        viewModel.unableToDeleteExerciseTypeWarningShown()
    }


    AppScreen(topAppRow = {
        Column(Modifier.fillMaxWidth()) {
            TopAppRow(
                startIcon = Icons.Filled.ArrowBack,
                onStartPressed = { navigator.popBackStack() },
                title = screenTitle
            )
        }
    }, bottomAppRow = {
        BottomAppRow {
                FloatingActionButton(
                    onClick = {
                        navigator.navigate(ExerciseTypeCreatorScreenDestination(ExerciseTypeDTO()))
                    }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = stringResource(id = R.string.create_new_exercise_type))
                }

                Text(
                    stringResource(id = R.string.create_new_exercise_type),
                    Modifier.padding(MaterialTheme.spacing.extraSmall),
                    style = MaterialTheme.typography.overline
                )

                Spacer(modifier = Modifier.weight(1f))

                val bgColor = if (selectedId.value == null) Color.Gray else MaterialTheme.colors.secondary

                FloatingActionButton(
                    onClick = {
                        selectedId.value?.let {
                            viewModel.setChosenExerciseTypeId(it)
                            navigator.navigate(WorkoutSetCreatorStep2Destination(viewModel.workoutSet.value!!))
                        }
                    },
                    backgroundColor = bgColor
                ) {

                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = stringResource(id = R.string.continue_to_step_2),
                    )
                }
        }
    }, showBannerAd = true) { modifier ->

        WorkoutSetCreatorStep1Content(
            exerciseTypes = exerciseTypes.value ?: emptyList(),
            selectedId = selectedId.value,
            onItemSelected = {
                viewModel.setChosenExerciseTypeId(it)
            },
            onDropDownSelected = { id, action ->
                when (action) {
                    DropDownAction.DELETE -> {
                        id?.let {
                            viewModel.deleteExerciseTypeById(
                                id, workoutDTO.workoutSets
                            )
                        }
                    }

                    DropDownAction.EDIT -> {
                        viewModel.setChosenExerciseTypeId(id)
                        navigator.navigate(ExerciseTypeCreatorScreenDestination(exerciseTypes.value!!.single { it.id == id }))
                    }

                    else -> { /* Do Nothing */
                    }
                }

            },
            onSearchFilterChanged = viewModel::setFilterText,
            onOrderChanged = viewModel::changeSortOrder,
            modifier = modifier
        )
    }


    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                val completedWorkoutSet = result.value
                resultBackNavigator.navigateBack(result = completedWorkoutSet)
            }
        }
    }

    etResultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                viewModel.setChosenExerciseTypeId(result.value)
            }
        }
    }
}

@Composable
private fun WorkoutSetCreatorStep1Content(
    exerciseTypes: List<ExerciseTypeDTO>,
    selectedId: Long?,
    onItemSelected: (id: Long) -> Unit,
    onDropDownSelected: (id: Long?, action: DropDownAction) -> Unit,
    onSearchFilterChanged: (search: String) -> Unit,
    onOrderChanged: () -> Unit,
    modifier: Modifier
) {
    Column(modifier.fillMaxWidth()) {
        TopBar(
            onSearchFilterChanged = onSearchFilterChanged, onOrderChanged = onOrderChanged
        )

        ExerciseTypeGrid(
            exerciseTypes = exerciseTypes,
            selectedId = selectedId,
            onItemSelected = onItemSelected,
            onDropDownSelected = onDropDownSelected,
            Modifier.padding(MaterialTheme.spacing.small)
        )
    }
}

@Composable
private fun ExerciseTypeGrid(
    exerciseTypes: List<ExerciseTypeDTO>,
    selectedId: Long?,
    onItemSelected: (id: Long) -> Unit,
    onDropDownSelected: (id: Long, action: DropDownAction) -> Unit,
    modifier: Modifier,
) {
    val density = LocalDensity.current

    var selected = if (selectedId == null) 0 else exerciseTypes.indexOfFirst { it.id == selectedId }
    if (selected == -1) selected = 0

    val gridState = rememberLazyGridState(selected)

    val dropDownItems = mutableListOf(
        DropDownItem(R.string.edit, DropDownAction.EDIT),
        DropDownItem(R.string.delete, DropDownAction.DELETE)
    )

    var contentMenuForId by rememberSaveable {
        mutableStateOf(-1L)
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp), modifier = modifier, state = gridState
    ) {
        items(exerciseTypes, key = { et -> et.id!! }) { exerciseType ->

            var pressOffset by remember {
                mutableStateOf(DpOffset.Zero)
            }
            var itemHeight by remember {
                mutableStateOf(0.dp)
            }
            val interactionSource = remember {
                MutableInteractionSource()
            }


            val backgroundColor = when (val backgroundColorHex = exerciseType.colorHex) {
                null -> android.graphics.Color.GRAY
                else -> android.graphics.Color.parseColor(
                    backgroundColorHex
                )
            }

            val fadeColor = Color(ColorUtils.setAlphaComponent(backgroundColor, 100))

            val cornerSize = MaterialTheme.spacing.extraExtraExtraLarge
            val bgShape = RoundedCornerShape(CornerSize(cornerSize / 10.0f))

            var boxMod = Modifier
                .padding(MaterialTheme.spacing.small)
                .fillMaxWidth(1f)

            if (exerciseType.id == selectedId) boxMod = boxMod.border(
                BorderStroke(4.dp, MaterialTheme.colors.primary), bgShape
            )

            Box(
                boxMod
            ) {
                Column(Modifier
                    .fillMaxSize()
                    .background(
                        fadeColor, bgShape
                    )
                    .padding(MaterialTheme.spacing.small)
                    .indication(
                        interactionSource, LocalIndication.current
                    )
                    .onSizeChanged {
                        with(density) {
                            itemHeight = it.height.toDp()
                        }
                    }
                    .pointerInput(true) {
                        detectTapGestures(onLongPress = {
                            contentMenuForId = exerciseType.id!!
                            pressOffset = DpOffset(
                                it.x.toDp(), it.y.toDp()
                            )
                        }, onPress = {
                            val press = PressInteraction.Press(it)
                            interactionSource.emit(press)
                            tryAwaitRelease()
                            interactionSource.emit(PressInteraction.Release(press))
                        }, onTap = {
                            onItemSelected(exerciseType.id!!)
                        })
                    }, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val drawableRes = getDrawableId(exerciseType.iconName!!, LocalContext.current)

                    Icon(
                        painterResource(id = drawableRes),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(
                                Color(backgroundColor), CircleShape
                            )
                            .padding(MaterialTheme.spacing.small)
                    )

                    Text(
                        exerciseType.name ?: "",
                        Modifier
                            .height(MaterialTheme.spacing.extraExtraLarge)
                            .padding(MaterialTheme.spacing.small),
                        style = MaterialTheme.typography.caption,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }

                /* TODO FUTURE VERSION animate
                        I tried with AnimatedVisibility and a nested Box with the Icon inside it.
                        Couldnt get it to fill the size properly so this will do for now (without anim)
                    */
                if (exerciseType.id == selectedId) {
                    Icon(
                        Icons.Filled.Check, contentDescription = null, // TODO
                        Modifier
                            .align(Alignment.BottomEnd)
                            .background(
                                MaterialTheme.colors.primary.copy(1f), CircleShape.copy(
                                    CornerSize(cornerSize / 6.0f)
                                )
                            ), tint = MaterialTheme.colors.onPrimary
                    )
                }

                DropdownMenu(
                    expanded = contentMenuForId == exerciseType.id,
                    onDismissRequest = { contentMenuForId = -1L },
                    offset = pressOffset.copy(x = pressOffset.x, y = pressOffset.y - itemHeight)
                ) {
                    dropDownItems.forEach { item ->
                        DropdownMenuItem(onClick = {
                            onDropDownSelected(exerciseType.id!!, item.action)
                            contentMenuForId = -1L
                        }) {
                            Text(stringResource(id = item.text))
                        }
                    }
                }
            }

        }
    }
}


@Composable
private fun TopBar(
    onSearchFilterChanged: (search: String) -> Unit, onOrderChanged: () -> Unit
) {

    var filter by remember { mutableStateOf("") }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val focusManager = LocalFocusManager.current

        BasicTextField(
            value = filter, onValueChange = {
                if (it.length <= 20) {
                    filter = it
                    onSearchFilterChanged(it)
                }
            }, singleLine = true, modifier = Modifier.weight(1f)
        ) { innerTextField ->


            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Search, contentDescription = null
                )

                innerTextField()

                Spacer(modifier = Modifier.weight(1f))

                if (filter.isNotEmpty()) Icon(Icons.Filled.Close,
                    contentDescription = null,
                    Modifier.clickable {
                        filter = ""
                        onSearchFilterChanged(filter)
                        focusManager.clearFocus()
                    })
            }
        }

        Modifier.weight(1f)
        Icon(Icons.Filled.SortByAlpha,
            contentDescription = null,
            Modifier.clickable { onOrderChanged() })
    }
}

@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Preview(name = "Phone", device = Devices.PIXEL_4_XL)
@Composable
private fun Preview() {
    val list = mutableListOf<ExerciseTypeDTO>()
    list.add(
        ExerciseTypeDTO(
            0, "Example ET", "et_icon_dumbbell", "#008296"
        )
    )
    list.add(
        ExerciseTypeDTO(
            1, "Number 1 ET", "et_icon_run", "#FFEA4DB0"
        )
    )
    list.add(
        ExerciseTypeDTO(
            2, "Bicep Curls", "et_icon_dumbbell", "#FFEA4DB0"
        )
    )
    list.add(
        ExerciseTypeDTO(
            3, "Burpees", "et_icon_flash", "#FF039BE5"
        )
    )
    list.add(
        ExerciseTypeDTO(
            4, "Jump Rope", "et_icon_jump_rope", "#FFFD5C29"
        )
    )

    AppTheme {
        WorkoutSetCreatorStep1Content(exerciseTypes = list,
            selectedId = 3,
            onItemSelected = {},
            onDropDownSelected = { _, _ -> },
            onSearchFilterChanged = {},
            onOrderChanged = {},
            Modifier
        )

    }
}