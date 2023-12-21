package com.softwareoverflow.hiit_trainer.ui.utils.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.softwareoverflow.hiit_trainer.ui.theme.spacing


@Composable
fun DialogOverlay(
    @DrawableRes icon: Int,
    @StringRes title: Int,
    @StringRes negativeButtonText: Int?,
    onNegativePress: (() -> Unit)?,
    @StringRes positiveButtonText: Int?,
    onPositivePress: (() -> Unit)?,
    content: @Composable ColumnScope.(Modifier) -> Unit
) {
    DialogOverlayContent(
        icon = {
            Icon(
                painterResource(id = icon),
                contentDescription = null,
                Modifier
                    .background(
                        MaterialTheme.colors.primary,
                        CircleShape.copy(CornerSize(MaterialTheme.spacing.extraLarge))
                    )
                    .padding(MaterialTheme.spacing.extraSmall),
                tint = MaterialTheme.colors.onPrimary
            )
        },
        title = title,
        negativeButtonText = negativeButtonText,
        onNegativePress = onNegativePress,
        positiveButtonText = positiveButtonText,
        onPositivePress = onPositivePress,
        content = content
    )
}

@Composable
fun DialogOverlay(
    icon: ImageVector,
    @StringRes title: Int,
    @StringRes negativeButtonText: Int?,
    onNegativePress: (() -> Unit)?,
    @StringRes positiveButtonText: Int?,
    onPositivePress: (() -> Unit)?,
    content: @Composable ColumnScope.(Modifier) -> Unit
) {

    DialogOverlayContent(
        icon = {
            Icon(
                icon,
                icon.name,
                Modifier
                    .background(
                        MaterialTheme.colors.primary,
                        CircleShape.copy(CornerSize(MaterialTheme.spacing.extraLarge))
                    )
                    .padding(MaterialTheme.spacing.extraSmall),
                tint = MaterialTheme.colors.onPrimary
            )
        },
        title = title,
        negativeButtonText = negativeButtonText,
        onNegativePress = onNegativePress,
        positiveButtonText = positiveButtonText,
        onPositivePress = onPositivePress,
        content = content
    )
}

@Composable
private fun DialogOverlayContent(
    icon: @Composable () -> Unit,
    @StringRes title: Int,
    @StringRes negativeButtonText: Int?,
    onNegativePress: (() -> Unit)?,
    @StringRes positiveButtonText: Int?,
    onPositivePress: (() -> Unit)?,
    content: @Composable ColumnScope.(Modifier) -> Unit
) {
    Column(
        Modifier
            .wrapContentHeight()
            .background(Color.Transparent)
            .padding(MaterialTheme.spacing.small)
            .background(
                color = MaterialTheme.colors.primary.copy(alpha = 0.75f),
                shape = RoundedCornerShape(CornerSize(MaterialTheme.spacing.large / 2))
            )
    ) {

        Row(verticalAlignment = Alignment.Top) {
            icon()

            Spacer(modifier = Modifier.weight(1f))
            Text(
                stringResource(title),
                Modifier
                    .padding(start = MaterialTheme.spacing.extraSmall)
                    .align(Alignment.CenterVertically),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onPrimary
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        content(Modifier.padding(MaterialTheme.spacing.extraSmall))

        Row(
            Modifier.padding(vertical = MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (negativeButtonText != null && onNegativePress != null) Text(stringResource(id = negativeButtonText).uppercase(),
                Modifier
                    .clickable { onNegativePress() }
                    .weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.button,
                fontWeight = FontWeight.Bold)

            if (positiveButtonText != null && onPositivePress != null) Text(stringResource(id = positiveButtonText).uppercase(),
                Modifier
                    .clickable { onPositivePress() }
                    .weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.button,
                fontWeight = FontWeight.Bold)
        }
    }
}