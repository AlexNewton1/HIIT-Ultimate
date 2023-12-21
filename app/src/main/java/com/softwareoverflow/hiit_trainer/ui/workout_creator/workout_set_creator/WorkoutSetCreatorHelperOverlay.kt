package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.theme.AppTheme
import com.softwareoverflow.hiit_trainer.ui.theme.colorRecover
import com.softwareoverflow.hiit_trainer.ui.theme.colorRepeat
import com.softwareoverflow.hiit_trainer.ui.theme.colorRest
import com.softwareoverflow.hiit_trainer.ui.theme.colorWork
import com.softwareoverflow.hiit_trainer.ui.theme.spacing
import com.softwareoverflow.hiit_trainer.ui.utils.compose.DialogOverlay

@Composable
@Destination(style = DestinationStyle.Dialog::class)
fun WorkoutSetCreatorHelp(navigator: DestinationsNavigator) {

    DialogOverlay(
        icon = R.drawable.icon_heart_pulse,
        title = R.string.workout_set_help,
        negativeButtonText = R.string.close,
        onNegativePress = { navigator.popBackStack() },
        positiveButtonText = null,
        onPositivePress = null
    ) { modifier ->

        Column(modifier) {

            Text(
                stringResource(id = R.string.workout_set_help_details),
                style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.onPrimary)
            )

            Spacer(Modifier.height(MaterialTheme.spacing.small))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    IconTextColum(
                        icon = R.drawable.icon_fire,
                        text = R.string.work,
                        Modifier.weight(1f),
                        colorWork
                    )
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = null,
                        Modifier.weight(0.3f),
                        tint = MaterialTheme.colors.onPrimary
                    )
                    IconTextColum(
                        icon = R.drawable.icon_rest,
                        text = R.string.rest,
                        Modifier.weight(1f),
                        colorRest
                    )
                }

                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = null,
                    Modifier.weight(0.25f),
                    tint = MaterialTheme.colors.onPrimary
                )

                Row(
                    Modifier
                        .weight(1f), verticalAlignment = Alignment.CenterVertically
                ) {
                    IconTextColum(
                        icon = R.drawable.icon_fire,
                        text = R.string.work,
                        Modifier.weight(1f),
                        colorWork
                    )
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = null,
                        Modifier.weight(0.3f),
                        tint = MaterialTheme.colors.onPrimary
                    )
                    IconTextColum(
                        icon = R.drawable.icon_rest,
                        text = R.string.rest,
                        Modifier.weight(1f),
                        colorRest
                    )
                }

                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = null,
                    Modifier.weight(0.25f),
                    tint = MaterialTheme.colors.onPrimary
                )

                Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    IconTextColum(
                        icon = R.drawable.icon_fire,
                        text = R.string.work,
                        Modifier.weight(1f),
                        colorWork
                    )
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = null,
                        Modifier.weight(0.3f),
                        tint = MaterialTheme.colors.onPrimary
                    )
                    IconTextColum(
                        icon = R.drawable.icon_recover,
                        text = R.string.recover,
                        Modifier.weight(1f),
                        colorRecover
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconTextColumn(
                    icon = R.drawable.icon_repeat,
                    text = "1/3",
                    modifier = Modifier.weight(2.5f),
                    colorRepeat
                )

                Spacer(modifier = Modifier.weight(1f))

                IconTextColumn(
                    icon = R.drawable.icon_repeat,
                    text = "2/3",
                    modifier = Modifier.weight(2.5f),
                    colorRepeat
                )

                Spacer(modifier = Modifier.weight(1f))

                IconTextColumn(
                    icon = R.drawable.icon_repeat,
                    text = "3/3",
                    modifier = Modifier.weight(2.5f),
                    colorRepeat
                )
            }
        }
    }
}

@Composable
private fun IconTextColum(
    @DrawableRes icon: Int, @StringRes text: Int, modifier: Modifier, tint: Color
) {
    IconTextColumn(icon = icon, text = stringResource(id = text), modifier = modifier, tint)
}

@Composable
private fun IconTextColumn(@DrawableRes icon: Int, text: String, modifier: Modifier, tint: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Icon(
            painterResource(id = icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .background(
                    MaterialTheme.colors.background,
                    CircleShape.copy(CornerSize(MaterialTheme.spacing.extraExtraExtraExtraLarge))
                )
                .padding(MaterialTheme.spacing.extraExtraSmall)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onPrimary
        )
    }
}


@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Preview(name = "Phone", device = Devices.PIXEL_4_XL)
@Composable
private fun Preview() {
    AppTheme {
        WorkoutSetCreatorHelp(EmptyDestinationsNavigator)
    }
}
