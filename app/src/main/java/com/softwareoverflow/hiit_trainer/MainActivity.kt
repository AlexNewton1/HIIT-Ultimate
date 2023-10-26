package com.softwareoverflow.hiit_trainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.preference.PreferenceManager
import com.ramcosta.composedestinations.DestinationsNavHost
import com.softwareoverflow.hiit_trainer.ui.NavGraphs
import com.softwareoverflow.hiit_trainer.ui.consent.UserConsentManager
import com.softwareoverflow.hiit_trainer.ui.theme.AppTheme
import com.softwareoverflow.hiit_trainer.ui.upgrade.BillingViewModel
import com.softwareoverflow.hiit_trainer.ui.upgrade.MobileAdsManager
import com.softwareoverflow.hiit_trainer.ui.utils.InAppReviewManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var adsManager: MobileAdsManager

    @Inject
    lateinit var billingClient: BillingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserConsentManager(this)

        setContent {
            AppTheme {

                val appState = rememberAppState()

                Scaffold(
                    scaffoldState = appState.scaffoldState,
                    snackbarHost = {
                        // reuse default SnackbarHost to have default animation and timing handling
                        SnackbarHost(it) { data ->
                            // custom snackbar with the custom colors
                            Snackbar(
                                actionColor = MaterialTheme.colors.secondary,
                                contentColor = MaterialTheme.colors.onPrimary,
                                backgroundColor = MaterialTheme.colors.onBackground,
                                snackbarData = data
                            )
                        }
                    }
                )
                { paddingValues ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        HomeScreenContent(appState)
                    }
                }

            }
        }

        // Create the InAppReviewManager
        InAppReviewManager.createReviewManager(this)
    }

    override fun onPause() {
        MobileAdsManager.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        if (this::billingClient.isInitialized) billingClient.queryPurchases()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (prefs.getBoolean("firstRun", true)) {
            MobileAdsManager.isFirstRun = true
            prefs.edit().putBoolean("firstRun", false).apply()
        }

        MobileAdsManager.resume()
    }

    override fun onDestroy() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (prefs.getBoolean("firstRun", true)) {
            prefs.edit().putBoolean("firstRun", false).apply()
        }

        MobileAdsManager.destroy()

        super.onDestroy()
    }
}


@Composable
private fun HomeScreenContent(appState: AppState) {

    DestinationsNavHost(
        navGraph = NavGraphs.root,
        navController = appState.navController,
    )

}

@Preview
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreenContent(rememberAppState())
    }
}