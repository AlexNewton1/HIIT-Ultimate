package com.softwareoverflow.hiit_trainer.ui.consent

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.navigation.NonDismissableDialog
import com.softwareoverflow.hiit_trainer.ui.theme.AppTheme
import com.softwareoverflow.hiit_trainer.ui.utils.compose.DialogOverlay

@Destination(style = NonDismissableDialog::class)
@Composable
fun UserConsentScreen(navigator: DestinationsNavigator) {
    val context = LocalContext.current

    DialogOverlay(icon = R.drawable.icon_heart_pulse,
        title = R.string.user_consent_title,
        negativeButtonText = null,
        onNegativePress = null,
        positiveButtonText = R.string.agree,
        onPositivePress = {
            UserConsentManager.userGaveConsent(context.applicationContext)
            navigator.popBackStack()
        }) { modifier ->

        val policyTag = "policy"
        val uriHandler = LocalUriHandler.current
        val annotatedText = buildAnnotatedString {
            append(stringResource(id = R.string.user_consent_details_1))

            pushStringAnnotation(tag = policyTag, annotation = "https://sites.google.com/view/software-overflow/hiit-trainer-privacy-policy")
            withStyle(style = SpanStyle(color = MaterialTheme.colors.secondary)) {
                append(stringResource(id = R.string.privacy_policy))
            }
            pop()

            append(stringResource(id = R.string.user_consent_details_2))
        }

        ClickableText(
            annotatedText,
            onClick = {
                annotatedText.getStringAnnotations(policyTag, it, it).firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
            },
            modifier = modifier,
            style = MaterialTheme.typography.caption.copy(MaterialTheme.colors.onPrimary)
        )

    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        UserConsentScreen(navigator = EmptyDestinationsNavigator)
    }
}