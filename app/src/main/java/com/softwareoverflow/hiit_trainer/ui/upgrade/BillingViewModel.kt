/**
 * Copyright (C) 2018 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.softwareoverflow.hiit_trainer.ui.upgrade

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.softwareoverflow.hiit_trainer.billing.ProUpgrade
import com.softwareoverflow.hiit_trainer.repository.billing.BillingRepository
import com.softwareoverflow.hiit_trainer.ui.view.LoadingSpinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import timber.log.Timber

/**
 * Notice just how small and simple this BillingViewModel is!!
 *
 * This beautiful simplicity is the result of keeping all the hard work buried inside the
 * [BillingRepository] and only inside the [BillingRepository]. The rest of your app
 * is now free from [BillingClient] tentacles!! And this [BillingViewModel] is the one and only
 * object the rest of your Android team need to know about billing.
 *
 */
class BillingViewModel(application: Application) : AndroidViewModel(application) {

    private val _proUpgradeLiveData: LiveData<ProUpgrade>
    val userHasUpgraded :LiveData<Boolean>

    private val viewModelScope = CoroutineScope(Job() + Dispatchers.Main)
    private val repository: BillingRepository = BillingRepository.getInstance(application)

    init {
        repository.startDataSourceConnections()

        _proUpgradeLiveData = repository.proUpgradeLiveData
        userHasUpgraded = Transformations.map(_proUpgradeLiveData) {
            it?.entitled ?: false
        }
    }

    /**
     * Query the users purchases. Done when the app is returned to (e.g. after navigating away to buy the product.
     * This will enable immediate (hopefully) removal of adverts and addition of remaining workout slots
     */
    fun queryPurchases() = repository.queryPurchasesAsync()

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared")
        repository.endDataSourceConnections()
        viewModelScope.coroutineContext.cancel()
    }

    fun purchasePro(activity: Activity) {
        try {
            LoadingSpinner.showLoadingIcon()
            repository.upgradeToPro(activity)
        } catch (e: NoSuchElementException) {
            Timber.w(e, "Unable to upgrade to pro.")
        }

        LoadingSpinner.hideLoadingIcon()
    }

    fun getMaxWorkoutSlots(): Int = repository.getMaxWorkoutSlots()
}