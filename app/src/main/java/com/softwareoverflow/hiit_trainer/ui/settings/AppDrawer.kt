package com.softwareoverflow.hiit_trainer.ui.settings

import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softwareoverflow.hiit_trainer.BuildConfig
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.theme.AppTheme
import com.softwareoverflow.hiit_trainer.ui.theme.spacing
import com.softwareoverflow.hiit_trainer.ui.utils.compose.LockScreenOrientation

@Composable
fun AppDrawer(
    openSettings: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxHeight()
    ) {
        Box(
            Modifier
                .width(IntrinsicSize.Min)
                .aspectRatio(1f)
                .background(MaterialTheme.colors.primary.copy(alpha = 0.5f))
        ) {
            Image(
                painterResource(id = R.drawable.icon_heart_pulse),
                null,
                Modifier.fillMaxSize(),
                alignment = Alignment.Center
            )

            Text(
                "${stringResource(R.string.version)} ${BuildConfig.VERSION_NAME}",
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(4.dp),
                style = MaterialTheme.typography.caption
            )
        }
        Spacer(Modifier.weight(1f))

        Row(
            Modifier
                .padding(vertical = 8.dp)
                .clickable { openSettings() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Settings,
                Icons.Filled.Settings.name,
                Modifier
                    .size(MaterialTheme.spacing.large)
                    .padding(end = 4.dp),
                tint = MaterialTheme.colors.onBackground
            )
            Text(
                stringResource(R.string.settings), color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Preview(name = "Phone", device = Devices.PIXEL_4_XL)
@Composable
private fun Preview() {
    LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    AppTheme {
        AppDrawer({})
    }
}