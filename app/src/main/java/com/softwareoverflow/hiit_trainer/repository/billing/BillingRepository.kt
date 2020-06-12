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

package com.softwareoverflow.hiit_trainer.repository.billing

import android.app.Activity
import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.billingclient.api.*
import com.softwareoverflow.hiit_trainer.billing.AugmentedSkuDetails
import com.softwareoverflow.hiit_trainer.billing.Entitlement
import com.softwareoverflow.hiit_trainer.billing.LocalBillingDb
import com.softwareoverflow.hiit_trainer.billing.ProUpgrade
import com.softwareoverflow.hiit_trainer.repository.billing.BillingRepository.GameSku.INAPP_SKUS
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

/**
 * @param application the [Application] context
 */
class BillingRepository private constructor(private val application: Application) :
    PurchasesUpdatedListener, BillingClientStateListener {

    /**
     * The [BillingClient] is the most reliable and primary source of truth for all purchases
     * made through the Google Play Store. The Play Store takes security precautions in guarding
     * the data. Also, the data is available offline in most cases, which means the app incurs no
     * network charges for checking for purchases using the [BillingClient]. The offline bit is
     * because the Play Store caches every purchase the user owns, in an
     * [eventually consistent manner](https://developer.android.com/google/play/billing/billing_library_overview#Keep-up-to-date).
     * This is the only billing client an app is actually required to have on Android. The other
     * two (webServerBillingClient and localCacheBillingClient) are optional.
     *
     * ASIDE. Notice that the connection to [playStoreBillingClient] is created using the
     * applicationContext. This means the instance is not [Activity]-specific. And since it's also
     * not expensive, it can remain open for the life of the entire [Application]. So whether it is
     * (re)created for each [Activity] or [Fragment] or is kept open for the life of the application
     * is a matter of choice.
     */
    private lateinit var playStoreBillingClient: BillingClient

    /**
     * A local cache billing client is important in that the Play Store may be temporarily
     * unavailable during updates. In such cases, it may be important that the users
     * continue to get access to premium data that they own. Alternatively, you may choose not to
     * provide offline access to your premium content.
     *
     * Even beyond offline access to premium content, however, a local cache billing client makes
     * certain transactions easier. Without an offline cache billing client, for instance, the app
     * would need both the secure server and the Play Billing client to be available in order to
     * process consumable products.
     *
     * The data that lives here should be refreshed at regular intervals so that it reflects what's
     * in the Google Play Store.
     */
    private lateinit var localCacheBillingClient: LocalBillingDb

    /**
     * This list tells clients what in-app products are available for sale
     */
    val inAppSkuDetailsListLiveData: LiveData<List<AugmentedSkuDetails>> by lazy {
        if (!::localCacheBillingClient.isInitialized) {
            localCacheBillingClient = LocalBillingDb.getInstance(application)
        }
        localCacheBillingClient.skuDetailsDao().getInappSkuDetails()
    }

    /**
     * Tracks whether this user is entitled to the PRO features. This call returns data from the app's
     * own local DB; this way if Play and the secure server are unavailable, users still have
     * access to features they purchased.  Normally this would be a good place to update the local
     * cache to make sure it's always up-to-date. However, onBillingSetupFinished already called
     * queryPurchasesAsync for you; so no need.
     */
    val proUpgradeLiveData: LiveData<ProUpgrade> by lazy {
        if (!::localCacheBillingClient.isInitialized) {
            localCacheBillingClient = LocalBillingDb.getInstance(application)
        }
        localCacheBillingClient.entitlementsDao().getProUpgrade()
    }

    /**
     * The maximum number of workout slots on free mode
     */
    val maxFreeWorkoutSlots = 3

    /**
     * Correlated data sources belong inside a repository module so that the rest of
     * the app can have appropriate access to the data it needs. Still, it may be effective to
     * track the opening (and sometimes closing) of data source connections based on lifecycle
     * events. One convenient way of doing that is by calling this
     * [startDataSourceConnections] when the [BillingViewModel] is instantiated and
     * [endDataSourceConnections] inside [ViewModel.onCleared]
     */
    fun startDataSourceConnections() {
        Timber.d("startDataSourceConnections")
        instantiateAndConnectToPlayBillingService()
        localCacheBillingClient = LocalBillingDb.getInstance(application)
    }

    fun endDataSourceConnections() {
        playStoreBillingClient.endConnection()
        // normally you don't worry about closing a DB connection unless you have more than
        // one DB open. so no need to call 'localCacheBillingClient.close()'
        Timber.d("endDataSourceConnections")
    }

    private fun instantiateAndConnectToPlayBillingService() {
        playStoreBillingClient = BillingClient.newBuilder(application.applicationContext)
            .enablePendingPurchases() // required or app will crash
            .setListener(this).build()
        connectToPlayBillingService()
    }

    private fun connectToPlayBillingService(): Boolean {
        Timber.d("connectToPlayBillingService")
        if (!playStoreBillingClient.isReady) {
            playStoreBillingClient.startConnection(this)
            return true
        }
        return false
    }

    /**
     * This is the callback for when connection to the Play [BillingClient] has been successfully
     * established. It might make sense to get [SkuDetails] and [Purchases][Purchase] at this point.
     */
    override fun onBillingSetupFinished(billingResult: BillingResult) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                Timber.d("onBillingSetupFinished successfully")
                querySkuDetailsAsync(BillingClient.SkuType.INAPP, GameSku.INAPP_SKUS)
                queryPurchasesAsync()
            }
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {
                //Some apps may choose to make decisions based on this knowledge.
                // TODO look at this
                Timber.d(billingResult.debugMessage)
            }
            else -> {
                //do nothing. Someone else will connect it through retry policy.
                //May choose to send to server though
                // TODO look at this
                Timber.d(billingResult.debugMessage)
            }
        }
    }

    /**
     * This method is called when the app has inadvertently disconnected from the [BillingClient].
     * An attempt should be made to reconnect using a retry policy. Note the distinction between
     * [endConnection][BillingClient.endConnection] and disconnected:
     * - disconnected means it's okay to try reconnecting.
     * - endConnection means the [playStoreBillingClient] must be re-instantiated and then start
     *   a new connection because a [BillingClient] instance is invalid after endConnection has
     *   been called.
     **/
    override fun onBillingServiceDisconnected() {
        Timber.d("onBillingServiceDisconnected")
        connectToPlayBillingService()
    }

    /**
     * BACKGROUND
     *
     * Google Play Billing refers to receipts as [Purchases][Purchase]. So when a user buys
     * something, Play Billing returns a [Purchase] object that the app then uses to release the
     * [Entitlement] to the user. Receipts are pivotal within the [BillingRepository]; but they are
     * not part of the repo’s public API, because clients don’t need to know about them. When
     * the release of entitlements occurs depends on the type of purchase. For consumable products,
     * the release may be deferred until after consumption by Google Play; for non-consumable
     * products and subscriptions, the release may be deferred until after
     * [BillingClient.acknowledgePurchaseAsync] is called. You should keep receipts in the local
     * cache for augmented security and for making some transactions easier.
     *
     * THIS METHOD
     *
     * [This method][queryPurchasesAsync] grabs all the active purchases of this user and makes them
     * available to this app instance. Whereas this method plays a central role in the billing
     * system, it should be called at key junctures, such as when user the app starts.
     *
     * Because purchase data is vital to the rest of the app, this method is called each time
     * the [BillingViewModel] successfully establishes connection with the Play [BillingClient]:
     * the call comes through [onBillingSetupFinished]. Recall also from Figure 4 that this method
     * gets called from inside [onPurchasesUpdated] in the event that a purchase is "already
     * owned," which can happen if a user buys the item around the same time
     * on a different device.
     */
    fun queryPurchasesAsync() {
        Timber.d("queryPurchasesAsync called")

        val purchasesResult = HashSet<Purchase>()
        val result = playStoreBillingClient.queryPurchases(BillingClient.SkuType.INAPP)

        Timber.d("queryPurchasesAsync INAPP results: ${result?.purchasesList?.size}")

        result?.purchasesList?.apply { purchasesResult.addAll(this) }

        processPurchases(purchasesResult)
    }

    private fun processPurchases(purchasesResult: Set<Purchase>) =
        CoroutineScope(Job() + Dispatchers.IO).launch {

            val validPurchases = HashSet<Purchase>(purchasesResult.size)
            Timber.d("processPurchases newBatch content $purchasesResult")

            purchasesResult.forEach { purchase ->
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    if (isSignatureValid(purchase)) {
                        validPurchases.add(purchase)
                    }
                } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                    Timber.d("Received a pending purchase of SKU: ${purchase.sku}")
                    // TODO look at this
                    // handle pending purchases, e.g. confirm with users about the pending
                    // purchases, prompt them to complete it, etc.
                }
            }

            /*
              As is being done in this sample, for extra reliability you may store the
              receipts/purchases to a your own remote/local database for until after you
              disburse entitlements. That way if the Google Play Billing library fails at any
              given point, you can independently verify whether entitlements were accurately
              disbursed. In this sample, the receipts are then removed upon entitlement
              disbursement.
             */
            val testing = localCacheBillingClient.purchaseDao().getPurchases()
            Timber.d("processPurchases purchases in the lcl db ${testing.size}")
            localCacheBillingClient.purchaseDao().insert(*validPurchases.toTypedArray())

            acknowledgeNonConsumablePurchasesAsync(validPurchases.toList())
        }

    /**
     * If you do not acknowledge a purchase, the Google Play Store will provide a refund to the
     * users within a few days of the transaction. Therefore you have to implement
     * [BillingClient.acknowledgePurchaseAsync] inside your app.
     */
    private fun acknowledgeNonConsumablePurchasesAsync(nonConsumables: List<Purchase>) {
        nonConsumables.forEach { purchase ->
            val params = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(
                purchase
                    .purchaseToken
            ).build()
            playStoreBillingClient.acknowledgePurchase(params) { billingResult ->
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        disburseNonConsumableEntitlement(purchase)
                    }
                    else -> Timber.d(
                        "acknowledgeNonConsumablePurchasesAsync response is ${billingResult.debugMessage}"
                    )
                }
            }
        }
    }

    /**
     * This is the final step, where purchases/receipts are converted to premium contents.
     * In this sample, once the entitlement is disbursed the receipt is thrown out.
     */
    private fun disburseNonConsumableEntitlement(purchase: Purchase) =
        CoroutineScope(Job() + Dispatchers.IO).launch {
            when (purchase.sku) {
                GameSku.PRO_UPGRADE -> {
                    val proUpgrade = ProUpgrade(true)
                    insert(proUpgrade)
                    localCacheBillingClient.skuDetailsDao()
                        .insertOrUpdate(purchase.sku, proUpgrade.mayPurchase())
                }
            }
            localCacheBillingClient.purchaseDao().delete(purchase)
        }

    /**
     * Ideally your implementation will comprise a secure server, rendering this check
     * unnecessary. @see [Security]
     */
    private fun isSignatureValid(purchase: Purchase): Boolean {
        return Security.verifyPurchase(
            Security.BASE_64_ENCODED_PUBLIC_KEY, purchase.originalJson, purchase.signature
        )
    }

    /**
     * Presumably a set of SKUs has been defined on the Google Play Developer Console. This
     * method is for requesting a (improper) subset of those SKUs. Hence, the method accepts a list
     * of product IDs and returns the matching list of SkuDetails.
     *
     * The result is passed to [onSkuDetailsResponse]
     */
    private fun querySkuDetailsAsync(
        @BillingClient.SkuType skuType: String,
        skuList: List<String>) {
        val params = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(skuType).build()
        Timber.d("querySkuDetailsAsync for $skuType")
        playStoreBillingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    if (skuDetailsList.orEmpty().isNotEmpty()) {
                        skuDetailsList!!.forEach {
                            CoroutineScope(Job() + Dispatchers.IO).launch {
                                localCacheBillingClient.skuDetailsDao().insertOrUpdate(it)
                            }
                        }
                    }
                }
                else -> {
                    Timber.d(billingResult.debugMessage)
                }
            }
        }
    }

    /**
     * This is the function to call when user wishes to make a purchase. This function will
     * launch the Google Play Billing flow. The response to this call is returned in
     * [onPurchasesUpdated]
     */
    fun launchBillingFlow(activity: Activity, augmentedSkuDetails: AugmentedSkuDetails) =
        launchBillingFlow(activity, SkuDetails(augmentedSkuDetails.originalJson!!))

    fun launchBillingFlow(activity: Activity, skuDetails: SkuDetails) {
        val purchaseParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build()
        playStoreBillingClient.launchBillingFlow(activity, purchaseParams)
    }

    /**
     * This method is called by the [playStoreBillingClient] when new purchases are detected.
     * The purchase list in this method is not the same as the one in
     * [queryPurchases][BillingClient.queryPurchases]. Whereas queryPurchases returns everything
     * this user owns, [onPurchasesUpdated] only returns the items that were just now purchased or
     * billed.
     *
     * The purchases provided here should be passed along to the secure server for
     * [verification](https://developer.android.com/google/play/billing/billing_library_overview#Verify)
     * and safekeeping. And if this purchase is consumable, it should be consumed, and the secure
     * server should be told of the consumption. All that is accomplished by calling
     * [queryPurchasesAsync].
     */
    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                // will handle server verification, consumables, and updating the local cache
                purchases?.apply { processPurchases(this.toSet()) }
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                // item already owned? call queryPurchasesAsync to verify and process all such items
                Timber.d(billingResult.debugMessage)
                queryPurchasesAsync()
            }
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> {
                connectToPlayBillingService()
            }
            else -> {
                Timber.d(billingResult.debugMessage)
            }
        }
    }

    @WorkerThread
    private suspend fun insert(entitlement: Entitlement) = withContext(Dispatchers.IO) {
        localCacheBillingClient.entitlementsDao().insert(entitlement)
    }

    companion object {
        @Volatile
        private var INSTANCE: BillingRepository? = null

        fun getInstance(application: Application): BillingRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: BillingRepository(application)
                        .also { INSTANCE = it }
            }
    }

    /**
     * [INAPP_SKUS]
     *
     * If you don't need customization ,then you can define these lists and hardcode them here.
     * That said, there are use cases where you may need customization:
     *
     * - If you don't want to update your APK (or Bundle) each time you change your SKUs, then you
     *   may want to load these lists from your secure server.
     *
     * - If your design is such that users can buy different items from different Activities or
     * Fragments, then you may want to define a list for each of those subsets. I only have two
     * subsets: INAPP_SKUS and SUBS_SKUS
     */
    private object GameSku {
        const val PRO_UPGRADE = "upgrade_to_pro" // TODO make sure this matches correctly

        val INAPP_SKUS = listOf(PRO_UPGRADE)
    }
}