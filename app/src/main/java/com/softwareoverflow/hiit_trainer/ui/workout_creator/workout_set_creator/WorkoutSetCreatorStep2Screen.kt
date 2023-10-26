package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.destinations.WorkoutSetCreatorHelpDestination
import com.softwareoverflow.hiit_trainer.ui.theme.AppTheme
import com.softwareoverflow.hiit_trainer.ui.theme.colorRecover
import com.softwareoverflow.hiit_trainer.ui.theme.colorRepeat
import com.softwareoverflow.hiit_trainer.ui.theme.colorRest
import com.softwareoverflow.hiit_trainer.ui.theme.colorWork
import com.softwareoverflow.hiit_trainer.ui.theme.spacing
import com.softwareoverflow.hiit_trainer.ui.utils.compose.AppScreen
import com.softwareoverflow.hiit_trainer.ui.utils.compose.TopAppRow
import com.softwareoverflow.hiit_trainer.ui.utils.compose.getDrawableId

@Destination
@Composable
fun WorkoutSetCreatorStep2(
    dto: WorkoutSetDTO,
    resultBackNavigator: ResultBackNavigator<WorkoutSetDTO>,
    navigator: DestinationsNavigator
) {

    val workoutSet = remember { mutableStateOf(dto) }

    AppScreen(topAppRow = {
        TopAppRow(startIcon = Icons.Filled.ArrowBack,
            onStartPressed = { navigator.popBackStack() },
            title = stringResource(id = R.string.nav_set_creator_step_2),
            endIcon = Icons.Filled.QuestionMark,
            onEndIconPressed = {
                navigator.navigate(WorkoutSetCreatorHelpDestination)
            })
    }, bottomAppRow = null, showBannerAd = true) { modifier ->

        WorkoutSetCreatorStep2Content(workoutSet = workoutSet.value,
            modifier = modifier,
            onFinished = { workTime, restTime, numReps, recoverTime ->
                val result = workoutSet.value.copy(
                    workTime = workTime,
                    restTime = restTime,
                    numReps = numReps,
                    recoverTime = recoverTime
                )

                resultBackNavigator.navigateBack(result = result)
            })
    }
}

@Composable
private fun WorkoutSetCreatorStep2Content(
    workoutSet: WorkoutSetDTO,
    modifier: Modifier,
    onFinished: (workTime: Int, restTime: Int, numReps: Int, recoverTime: Int) -> Unit
) {
    var workTime by remember { mutableStateOf(workoutSet.workTime) }
    var workTimeError by remember { mutableStateOf(false) }

    var restTime by remember { mutableStateOf(workoutSet.restTime) }
    var restTimeError by remember { mutableStateOf(false) }

    var numReps by remember { mutableStateOf(workoutSet.numReps) }
    var numRepsError by remember { mutableStateOf(false) }

    var recoverTime by remember { mutableStateOf(workoutSet.recoverTime) }
    var recoverTimeError by remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.small)
    ) {

        val iconColor = android.graphics.Color.parseColor(
            workoutSet.exerciseTypeDTO!!.colorHex!!
        )

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(
                    id = getDrawableId(
                        workoutSet.exerciseTypeDTO!!.iconName!!, LocalContext.current
                    )
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(MaterialTheme.spacing.extraExtraExtraLarge)
                    .padding(MaterialTheme.spacing.small)
                    .background(Color(iconColor), CircleShape)
                    .padding(MaterialTheme.spacing.small)

            )
            Text(
                workoutSet.exerciseTypeDTO!!.name!!,
                Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.small),
                style = MaterialTheme.typography.h5,
                maxLines = 2

            )
        }

        Row(
            Modifier
                .weight(1f)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconInput(initialValue = workTime,
                drawableId = R.drawable.icon_fire,
                tint = colorWork.copy(alpha = 0.75f),
                name = stringResource(id = R.string.work_time),
                modifier = Modifier.weight(1f),
                onValueChange = { value, error ->
                    workTime = value
                    workTimeError = error
                })

            IconInput(initialValue = restTime,
                drawableId = R.drawable.icon_rest,
                tint = if (numReps <= 1) Color.Gray else colorRest.copy(alpha = 0.75f),
                name = stringResource(id = R.string.rest_time),
                modifier = Modifier.weight(1f),
                onValueChange = { value, error ->
                    restTime = value
                    restTimeError = error
                })
        }


        Row(
            Modifier
                .weight(1f)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconInput(initialValue = numReps,
                drawableId = R.drawable.icon_repeat,
                tint = colorRepeat.copy(alpha = 0.75f),
                name = stringResource(id = R.string.num_reps),
                modifier = Modifier.weight(1f),
                onValueChange = { value, error ->
                    numReps = value
                    numRepsError = error
                })

            IconInput(initialValue = recoverTime,
                drawableId = R.drawable.icon_recover,
                tint = colorRecover.copy(alpha = 0.75f),
                name = stringResource(id = R.string.recovery_time),
                modifier = Modifier.weight(1f),
                onValueChange = { value, error ->
                    recoverTime = value
                    recoverTimeError = error
                })
        }

        Spacer(modifier = Modifier.weight(0.5f))

        val isError = workTimeError || restTimeError || numRepsError || recoverTimeError
        val bgColor = if (isError) Color.Gray else MaterialTheme.colors.secondary

        FloatingActionButton(onClick = {
            if (!isError) onFinished(workTime, restTime, numReps, recoverTime)
        }, Modifier.align(Alignment.End), backgroundColor = bgColor, shape = CircleShape) {
            Icon(Icons.Filled.Check, contentDescription = stringResource(id = R.string.content_desc_add_set_to_workout))
        }
    }
}

@Composable
private fun IconInput(
    initialValue: Int,
    drawableId: Int,
    tint: Color,
    name: String,
    modifier: Modifier,
    onValueChange: (Int, Boolean) -> Unit
) {

    var input by remember { mutableStateOf(initialValue) }
    var isError by remember { mutableStateOf(false) }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(MaterialTheme.spacing.extraExtraExtraLarge),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(id = drawableId),
                contentDescription = null,
                Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.medium),
                tint = tint
            )

            BasicTextField(
                value = input.toString(),
                onValueChange = {
                    if (it.length <= 3) {
                        try {
                            input = it.toInt()
                            isError = input <= 0
                        } catch (_: NumberFormatException) {
                            isError = true
                        }

                        onValueChange(input, isError)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(
                        IntrinsicSize.Min
                    )
                    .padding(MaterialTheme.spacing.extraExtraSmall),
                decorationBox = { innerTextField ->
                    Column(Modifier.fillMaxWidth()) {
                        innerTextField()
                        Divider(
                            Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = if (isError) MaterialTheme.colors.error else MaterialTheme.colors.onSurface
                        )
                    }
                },
                textStyle = MaterialTheme.typography.h4
            )

            Text(
                name,
                Modifier
                    .padding(MaterialTheme.spacing.small)
                    .align(Alignment.BottomCenter),
                style = MaterialTheme.typography.caption
            )
        }

    }
}

@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Preview(name = "Phone", device = Devices.PIXEL_4_XL)
@Composable
private fun Preview() {
    val workoutSet = WorkoutSetDTO(
        ExerciseTypeDTO(
            3, "Burpees", "et_icon_flash", "#FF039BE5"
        ), 40, 20, 6, 120
    )

    AppTheme {
        WorkoutSetCreatorStep2Content(workoutSet = workoutSet, Modifier, onFinished = { _, _, _, _ -> })
    }
}