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
import androidx.lifecycle.ViewModel
import com.softwareoverflow.hiit_trainer.repository.billing.BillingRepository
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
class BillingViewModel(application: Application) : ViewModel() {

    private val viewModelScope = CoroutineScope(Job() + Dispatchers.Main)
    private val repository: BillingRepository = BillingRepository.getInstance(application)

    init {
        repository.startDataSourceConnections()

        repository.proUpgradeLiveData.observeForever {

            it?.let {
                if(it.entitled) UpgradeManager.setUserUpgraded()
            }
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
            repository.upgradeToPro(activity)
        } catch (e: NoSuchElementException) {
            Timber.w(e, "Unable to upgrade to pro.")
        }
    }

    fun getMaxWorkoutSlots(): Int = repository.getMaxWorkoutSlots()
}