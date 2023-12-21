package com.softwareoverflow.hiit_trainer.ui.utils.compose

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.theme.colorRecover
import com.softwareoverflow.hiit_trainer.ui.theme.colorRepeat
import com.softwareoverflow.hiit_trainer.ui.theme.colorRest
import com.softwareoverflow.hiit_trainer.ui.theme.colorWork

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun CircleCheckbox(checked: Boolean, enabled: Boolean = true, onCheckedChange: (Boolean) -> Unit) {

    val color = MaterialTheme.colors
    val imageVector = if (checked) Icons.Filled.CheckCircle else Icons.Outlined.Circle
    val tint = if (checked) color.primary.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
    val background = if (checked) Color.White else Color.Transparent

    IconButton(onClick = { onCheckedChange(!checked) },
        enabled = enabled) {

        Icon(imageVector = imageVector, tint = tint,
            modifier = Modifier.background(background, shape = CircleShape),
            contentDescription = "checkbox")
    }
}

@Composable
fun AddCheckbox(checked: Boolean, enabled: Boolean = true, onCheckedChange: (Boolean) -> Unit) {

    val color = MaterialTheme.colors
    val imageVector = if (checked) Icons.Filled.AddCircle else Icons.Outlined.Circle
    val tint = if (checked) color.primary.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
    val background = if (checked) Color.White else Color.Transparent

    IconButton(onClick = { onCheckedChange(!checked) },
        enabled = enabled) {

        Icon(imageVector = imageVector, tint = tint,
            modifier = Modifier.background(background, shape = CircleShape),
            contentDescription = "checkbox")
    }
}


@Composable
fun KeepScreenOn() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = context.findActivity()?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}




@Composable
fun ColouredIcon(icon: ColouredIcon, modifier: Modifier = Modifier) {
    icon.let {
        Icon(
            painterResource(id = it.id),
            contentDescription = stringResource(id = it.contentDesc),
            tint = it.color,
            modifier = Modifier,
        )
    }
}

enum class ColouredIcon(@DrawableRes val id: Int, val color: Color, @StringRes val contentDesc: Int) {
    WORK(R.drawable.icon_fire, colorWork, R.string.content_desc_work_time),
    REST(R.drawable.icon_rest, colorRest, R.string.content_desc_rest_time),
    REPEAT(R.drawable.icon_repeat, colorRepeat, R.string.content_desc_num_reps),
    RECOVER(R.drawable.icon_recover, colorRecover, R.string.content_desc_recover_time)
}