package com.softwareoverflow.hiit_trainer.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.consent.UserConsentManager
import com.softwareoverflow.hiit_trainer.ui.upgrade.AdsManager
import com.softwareoverflow.hiit_trainer.ui.upgrade.BillingViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var adsManager: AdsManager

    private lateinit var billingClient: BillingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UserConsentManager(this)

        adsManager = AdsManager(this, bannerAdvert)

        navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        // Prevent nav gesture if not on start destination
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            // Lock / unlock the navigation drawer
            when (destination.id) {
                controller.graph.startDestination -> {
                    drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
                    adsManager.hideBanner()
                }
                R.id.userConsentDialog -> adsManager.hideBanner()
                R.id.upgradeDialog -> adsManager.hideBanner()
                R.id.settingsFragment -> {
                    adsManager.hideBanner()
                    drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
                }
                else -> {
                    drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
                    adsManager.showBanner()
                }
            }

            if(destination.id == R.id.workoutFragment)
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            else
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        billingClient = ViewModelProvider(this).get(BillingViewModel::class.java)
        billingClient.userHasUpgraded.observe(this, Observer {
            adsManager.setUserUpgraded(it)
        })

        NavigationUI.setupWithNavController(navView, navController)

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navMenuSettings -> findNavController(R.id.myNavHostFragment).navigate(R.id.action_homeScreenFragment_to_settingsFragment)
            R.id.navMenuFeedback -> launchEmailFeedback()
        }

        return true
    }

    /**
     * Provides support for NavigationUI navigateUp on back press, and provides any custom actions
     */
    override fun onSupportNavigateUp(): Boolean {
        val controller = findNavController(R.id.myNavHostFragment)

        return when (controller.currentDestination?.id) {
            R.id.workoutCompleteFragment -> {
                onBackPressed()
                //controller.navigate(R.id.action_workoutCompleteFragment_to_homeScreenFragment)
                true
            }
            R.id.workoutCreatorFragment -> {
                onBackPressed()
                true
            }
            else -> NavigationUI.navigateUp(navController, appBarConfiguration)
        }
    }

    private fun launchEmailFeedback(){
        val i = Intent(Intent.ACTION_SENDTO).apply {
            type = "text/plain"
            data = Uri.parse("mailto:SoftwareOverflow@gmail.com?subject=HIIT Trainer Feedback")
        }

        try {
            startActivity(Intent.createChooser(i, "Send email feedback"))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this@MainActivity,
                "There are no email clients installed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()

        if (::billingClient.isInitialized)
            billingClient.queryPurchases()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (prefs.getBoolean("firstRun", true)) {
            AdsManager.isFirstRun = true
            prefs.edit().putBoolean("firstRun", false).apply()
        }
    }

    override fun onDestroy() {
        adsManager.destroy()
        super.onDestroy()
    }
}
