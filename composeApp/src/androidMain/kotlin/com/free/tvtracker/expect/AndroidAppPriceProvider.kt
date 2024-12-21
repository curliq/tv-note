package com.free.tvtracker.expect

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetailsParams
import com.free.tvtracker.AndroidApplication
import com.free.tvtracker.data.iap.AppPriceProvider
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class AndroidAppPriceProvider(private val context: Context) : AppPriceProvider {

    private val appPriceId = "01"
    private val appSubId = "sub1"

    val billingClient by lazy {
        BillingClient.newBuilder(context)
            .enablePendingPurchases()
            .setListener { billingResult, purchases -> }
            .build()
    }

    override suspend fun appPrice(): String? = suspendCoroutine { continuation ->
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

                    val skuList = listOf(appPriceId)
                    val params = SkuDetailsParams.newBuilder()
                        .setSkusList(skuList)
                        .setType(BillingClient.SkuType.INAPP)
                        .build()

                    billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                            skuDetailsList != null
                        ) {
                            val appPrice = skuDetailsList.firstOrNull()
                            if (appPrice == null) {
                                continuation.resumeWith(Result.success(null))
                            } else {
                                val price = appPrice.price
                                continuation.resumeWith(Result.success(price))
                            }
                        } else {
                            continuation.resumeWith(Result.success(null))
                        }
                    }
                } else {
                    continuation.resumeWith(Result.success(null))
                }
            }

            override fun onBillingServiceDisconnected() {
                continuation.resumeWith(Result.success(null))
            }
        })
    }

    override suspend fun appSubPrice(): String? = suspendCoroutine { continuation ->
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val skuList = listOf(appSubId)
                    val params = SkuDetailsParams.newBuilder()
                        .setSkusList(skuList)
                        .setType(BillingClient.SkuType.SUBS)
                        .build()

                    billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                            skuDetailsList != null
                        ) {
                            val subPrice = skuDetailsList.firstOrNull()
                            if (subPrice == null) {
                                continuation.resumeWith(Result.success(null))
                            } else {
                                val price = subPrice.price
                                continuation.resumeWith(Result.success(price))
                            }
                        } else {
                            continuation.resumeWith(Result.success(null))
                        }
                    }
                } else {
                    continuation.resumeWith(Result.success(null))
                }
            }

            override fun onBillingServiceDisconnected() {
                continuation.resumeWith(Result.success(null))
            }
        })
    }

    override suspend fun buyApp(): Boolean = suspendCoroutine { continuation ->
        val billingClient = BillingClient.newBuilder(context)
            .setListener { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                    purchases?.firstOrNull() != null
                ) {
                    continuation.resumeWith(Result.success(true))
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                    continuation.resumeWith(Result.success(false))
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                    continuation.resumeWith(Result.success(true))
                } else {
                    continuation.resumeWith(Result.success(false))
                }
            }
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val skuList = listOf(appPriceId)
                    val params = SkuDetailsParams.newBuilder()
                        .setSkusList(skuList)
                        .setType(BillingClient.SkuType.INAPP)
                        .build()
                    billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                            skuDetailsList != null
                        ) {
                            for (skuDetail in skuDetailsList) {
                                val flowParams = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetail)
                                    .build()

                                val activity = AndroidApplication.instance.currentActivity
                                if (activity == null) {
                                    billingClient.endConnection()
                                    continuation.resumeWith(Result.success(false))
                                }
                                val billingResult = billingClient.launchBillingFlow(activity!!, flowParams)
                                if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                                    billingClient.endConnection()
                                    continuation.resumeWith(Result.success(false))
                                }
                            }
                        } else {
                            billingClient.endConnection()
                            continuation.resumeWith(Result.success(false))
                        }
                    }
                } else {
                    billingClient.endConnection()
                    continuation.resumeWith(Result.success(false))
                }
            }

            override fun onBillingServiceDisconnected() {
                billingClient.endConnection()
                continuation.resumeWith(Result.success(false))
            }
        })
    }

    override suspend fun subscribe(): Boolean = suspendCoroutine { continuation ->
        val billingClient = BillingClient.newBuilder(context)
            .setListener { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                    purchases?.firstOrNull() != null
                ) {
                    continuation.resumeWith(Result.success(true))
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                    continuation.resumeWith(Result.success(false))
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                    continuation.resumeWith(Result.success(true))
                } else {
                    continuation.resumeWith(Result.success(false))
                }
            }
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val skuList = listOf(appSubId)
                    val params = SkuDetailsParams.newBuilder()
                        .setSkusList(skuList)
                        .setType(BillingClient.SkuType.SUBS)
                        .build()
                    billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                            skuDetailsList != null
                        ) {
                            for (skuDetail in skuDetailsList) {
                                val flowParams = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetail)
                                    .build()

                                val activity = AndroidApplication.instance.currentActivity
                                if (activity == null) {
                                    billingClient.endConnection()
                                    continuation.resumeWith(Result.success(false))
                                }
                                val billingResult = billingClient.launchBillingFlow(activity!!, flowParams)
                                if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                                    billingClient.endConnection()
                                    continuation.resumeWith(Result.success(false))
                                }
                            }
                        } else {
                            billingClient.endConnection()
                            continuation.resumeWith(Result.success(false))
                        }
                    }
                } else {
                    billingClient.endConnection()
                    continuation.resumeWith(Result.success(false))
                }
            }

            override fun onBillingServiceDisconnected() {
                billingClient.endConnection()
                continuation.resumeWith(Result.success(false))
            }
        })
    }

    override suspend fun restorePurchase(): Boolean {
        return restoreOneTimePurchase() || restoreSubscription()
    }

    private suspend fun restoreOneTimePurchase(): Boolean = suspendCoroutine { continuation ->
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    billingClient.queryPurchasesAsync(
                        BillingClient.SkuType.INAPP
                    ) { billingResult, purchases ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            for (purchase in purchases) {
                                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                                    purchase.skus.any { sku -> sku == appPriceId }
                                ) {
                                    continuation.resume(true)
                                }
                            }
                        }
                        continuation.resume(false)
                    }
                } else {
                    continuation.resume(false)
                }
            }

            override fun onBillingServiceDisconnected() {
                continuation.resume(false)
            }
        })
    }

    private suspend fun restoreSubscription(): Boolean = suspendCoroutine { continuation ->
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    billingClient.queryPurchasesAsync(
                        BillingClient.SkuType.SUBS
                    ) { billingResult, purchases ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            for (purchase in purchases) {
                                if (purchase.skus.any { it == appSubId }) {
                                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED ||
                                        purchase.isAutoRenewing
                                    ) {
                                        continuation.resume(true)
                                    }
                                }
                            }
                        }
                        continuation.resume(false)
                    }
                } else {
                    continuation.resume(false)
                }
            }

            override fun onBillingServiceDisconnected() {
                continuation.resume(false)
            }
        })
    }
}
