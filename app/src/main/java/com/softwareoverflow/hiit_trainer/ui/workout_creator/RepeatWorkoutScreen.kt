package com.softwareoverflow.hiit_trainer.ui.workout_creator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.ui.theme.AppTheme
import com.softwareoverflow.hiit_trainer.ui.theme.spacing
import com.softwareoverflow.hiit_trainer.ui.utils.compose.DialogOverlay

@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun RepeatWorkoutDialog(workout: WorkoutDTO, resultBackNavigator: ResultBackNavigator<WorkoutDTO>) {
    var isRestBetweenError by remember { mutableStateOf(false) }
    var repeatPickerValue by remember { mutableStateOf(workout.numReps) }
    var restBetweenValue by remember { mutableStateOf(workout.recoveryTime) }

    DialogOverlay(icon = R.drawable.icon_repeat,
        title = R.string.repeat_workout,
        negativeButtonText = R.string.cancel,
        onNegativePress = { resultBackNavigator.navigateBack() },
        positiveButtonText = R.string.save,
        onPositivePress = {
            if (!isRestBetweenError) {
                workout.numReps = repeatPickerValue
                workout.recoveryTime = restBetweenValue

                resultBackNavigator.navigateBack(result = workout)
            }

        }) { modifier ->

        RepeatWorkoutDialogContent(repeatTimes = repeatPickerValue,
            restBetween = restBetweenValue,
            modifier,
            onRepsChange = { repeatPickerValue = it },
            onRecoverChange = { restBetweenValue = it },
            onErrorChange = { isRestBetweenError = it })
    }
}

@Composable
private fun RepeatWorkoutDialogContent(
    repeatTimes: Int,
    restBetween: Int,
    modifier: Modifier,
    onRepsChange: (Int) -> Unit,
    onRecoverChange: (Int) -> Unit,
    onErrorChange: (Boolean) -> Unit
) {
    Column(
        modifier
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(id = R.string.complete_workout),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.caption
            )

            NumberPicker(
                value = repeatTimes,
                range = 1..10,
                onValueChange = {
                    onRepsChange(it)
                },
                textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onPrimary),
                dividersColor = MaterialTheme.colors.secondary
            )

            Text(
                stringResource(id = R.string.times),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.caption
            )
        }

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(id = R.string.with),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.caption
            )

            BasicTextField(
                value = restBetween.toString(),
                onValueChange = {
                    if (it.length <= 3) {
                        try {
                            onRecoverChange(it.toInt())
                            onErrorChange(false)
                        } catch (_: NumberFormatException) {
                            onErrorChange(true)
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(
                        IntrinsicSize.Min
                    )
                    .padding(MaterialTheme.spacing.extraExtraSmall),
                decorationBox = { innerTextField ->
                    Column {
                        innerTextField()
                        Divider(
                            thickness = 1.dp
                        )
                    }
                },
                textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onPrimary)
            )

            Text(
                stringResource(id = R.string.s_rest_in_between),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Preview(name = "Phone", device = Devices.PIXEL_4_XL)
@Composable
private fun Preview() {
    AppTheme {
        RepeatWorkoutDialogContent(repeatTimes = 5, restBetween = 120, Modifier, {}, {}, {})
    }
}

