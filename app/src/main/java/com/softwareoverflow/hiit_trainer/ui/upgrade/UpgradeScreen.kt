package com.softwareoverflow.hiit_trainer.ui.upgrade

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.theme.AppTheme
import com.softwareoverflow.hiit_trainer.ui.utils.compose.DialogOverlay
import com.softwareoverflow.hiit_trainer.ui.utils.compose.findActivity


@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun UpgradeScreen(viewModel: UpgradeViewModel = hiltViewModel(), navigator: DestinationsNavigator) {
    val context = LocalContext.current

    Content(onCancel = {
        navigator.popBackStack()
    }, onUpgrade = {
        viewModel.upgrade(context.findActivity())
    })

}

@Composable
private fun Content(onCancel: () -> Unit, onUpgrade: () -> Unit) {
    DialogOverlay(icon = R.drawable.icon_heart_pulse,
        title = R.string.upgrade_to_pro,
        negativeButtonText = R.string.cancel,
        onNegativePress = { onCancel() },
        positiveButtonText = R.string.upgrade_now,
        onPositivePress = { onUpgrade() }) {
        Column(it.wrapContentHeight()) {

            Text(
                stringResource(id = R.string.upgrade_to_pro_benefits),
                style = MaterialTheme.typography.body2.copy(color = Color.White)
            )
            Text(
                stringResource(id = R.string.upgrade_to_pro_benefits_list),
                style = MaterialTheme.typography.body2.copy(color = Color.White)
            )
        }
    }
}

@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Preview(name = "Phone", device = Devices.PIXEL_4_XL)
@Composable
private fun Preview() {
    AppTheme {
        Content({}, {})
    }
}