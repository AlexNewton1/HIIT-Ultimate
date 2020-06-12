package com.softwareoverflow.hiit_trainer.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.upgrade.AdsManager
import com.softwareoverflow.hiit_trainer.ui.upgrade.BillingViewModel
import com.softwareoverflow.hiit_trainer.ui.view.LoadingSpinner
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var adsManager: AdsManager

    private val billingClient: BillingViewModel = ViewModelProvider(this).get(BillingViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO - show some information about what will be collected etc.
        adsManager = AdsManager(this, bannerAdvert)


        navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        // Prevent nav gesture if not on start destination
        navController.addOnDestinationChangedListener { controller, destination, bundle ->
            // Lock / unlock the navigation drawer
            when (destination.id) {
                controller.graph.startDestination -> {
                    drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
                    adsManager.hideBanner()
                }
                R.id.upgradeDialog -> {
                    adsManager.hideBanner()
                }
                else -> {
                    drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
                    adsManager.showBanner()
                }
            }
        }

        billingClient.userHasUpgraded.observe(this, Observer {
            adsManager.setUserUpgraded(it)
        })

        NavigationUI.setupWithNavController(navView, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        LoadingSpinner.initialise(menu!![0], applicationContext)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val controller = findNavController(R.id.myNavHostFragment)

        return when(controller.currentDestination?.id) {
            R.id.workoutCompleteFragment -> {
                controller.navigate(R.id.action_workoutCompleteFragment_to_homeScreenFragment)
                true
            }
            else -> NavigationUI.navigateUp(navController, appBarConfiguration)
        }
    }

    override fun onBackPressed() {
        val controller = findNavController(R.id.myNavHostFragment)
        if(controller.currentDestination?.id == R.id.workoutCompleteFragment){
            controller.navigate(R.id.action_workoutCompleteFragment_to_homeScreenFragment)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        billingClient.queryPurchases()
    }

    override fun onDestroy() {
        adsManager.destroy()
        super.onDestroy()
    }
}
