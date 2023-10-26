package com.softwareoverflow.hiit_trainer.ui.upgrade

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UpgradeManager {

    private val userUpgraded = MutableStateFlow(false)
    val userUpgradedFlow : StateFlow<Boolean> get() = userUpgraded

    fun isUserUpgraded() : Boolean {
        return userUpgraded.value
    }

    var proPrice: String? = null
        private set

    fun setUserUpgraded(){
        userUpgraded.value = true
    }

    fun setUpgradePrice(price: String) {
        proPrice = price
    }
}