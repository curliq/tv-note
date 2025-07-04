package com.free.tvtracker.expect

import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
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

    private var responseListener: (Boolean) -> Unit = { }

    val billingClient by lazy {
        BillingClient.newBuilder(context)
            .enablePendingPurchases()
            .setListener { billingResult, purchases ->
                val purchase = purchases?.firstOrNull()
                if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                    responseListener(true)
                }
                else if ((billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchase != null)) {
                    acknowledge(purchase.purchaseToken, responseListener)
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                    responseListener(false)
                } else {
                    responseListener(false)
                }
            }
            .build()
    }

    private fun acknowledge(token: String, listener: (Boolean) -> Unit) {
        val params =
            AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(token)
                .build()
        val acknowledgePurchaseResponseListener = object : AcknowledgePurchaseResponseListener {
            override fun onAcknowledgePurchaseResponse(p0: BillingResult) {
                listener(p0.responseCode == BillingClient.BillingResponseCode.OK)
            }
        }
        billingClient.acknowledgePurchase(params, acknowledgePurchaseResponseListener)
    }

    override suspend fun appPrice(): String? = suspendCoroutine { continuation ->
        var resumed = false
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
                resumed = true
            }

            override fun onBillingServiceDisconnected() {
                if (!resumed) {
                    continuation.resumeWith(Result.success(null))
                }
            }
        })
    }

    override suspend fun appSubPrice(): String? = suspendCoroutine { continuation ->
        var resumed = false
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
                resumed = true
            }

            override fun onBillingServiceDisconnected() {
                if (!resumed) {
                    continuation.resumeWith(Result.success(null))
                }
            }
        })
    }

    override suspend fun buyApp(): Boolean = suspendCoroutine { continuation ->
        var resumed = false
        fun resume(res: Boolean) {
            if (!resumed) {
                resumed = true
                continuation.resumeWith(Result.success(res))
            }
        }
        responseListener = { res -> resume(res) }
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
                                    resume(false)
                                }
                                val billingResult = billingClient.launchBillingFlow(activity!!, flowParams)
                                if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                                    resume(false)
                                }
                            }
                        } else {
                            resume(false)
                        }
                    }
                } else {
                    resume(false)
                }
            }

            override fun onBillingServiceDisconnected() {
                if (!resumed) {
                    resume(false)
                }
            }
        })
    }

    override suspend fun subscribe(): Boolean = suspendCoroutine { continuation ->
        var resumed = false
        fun resume(res: Boolean) {
            if (!resumed) {
                resumed = true
                continuation.resumeWith(Result.success(res))
            }
        }
        responseListener = { a -> resume(a) }
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
                                    resume(false)
                                }
                                val billingResult = billingClient.launchBillingFlow(activity!!, flowParams)
                                if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                                    resume(false)
                                }
                            }
                        } else {
                            resume(false)
                        }
                    }
                } else {
                    resume(false)
                }
            }

            override fun onBillingServiceDisconnected() {
                if (!resumed) {
                    resume(false)
                }
            }
        })
    }

    override suspend fun restorePurchase(): Boolean {
        return restoreOneTimePurchase() || restoreSubscription()
    }

    private suspend fun restoreOneTimePurchase(): Boolean = suspendCoroutine { continuation ->
        var resumed = false
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    billingClient.queryPurchasesAsync(
                        BillingClient.SkuType.INAPP
                    ) { billingResult, purchases ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            val purchase = purchases.firstOrNull {
                                it.purchaseState == Purchase.PurchaseState.PURCHASED &&
                                    it.skus.any { sku -> sku == appPriceId }
                            }
                            if (purchase != null) {
                                acknowledge(purchase.purchaseToken) {
                                    continuation.resume(it)
                                }
                            } else {
                                continuation.resume(false)
                            }
                        } else {
                            continuation.resume(false)
                        }
                    }
                } else {
                    continuation.resume(false)
                }
                resumed = true
            }

            override fun onBillingServiceDisconnected() {
                if (!resumed) {
                    continuation.resume(false)
                }
            }
        })
    }

    private suspend fun restoreSubscription(): Boolean = suspendCoroutine { continuation ->
        var resumed = false
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    billingClient.queryPurchasesAsync(
                        BillingClient.SkuType.SUBS
                    ) { billingResult, purchases ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

                            val purchase = purchases.firstOrNull {
                                it.purchaseState == Purchase.PurchaseState.PURCHASED &&
                                    it.skus.any { sku -> sku == appSubId }
                            }
                            if (purchase != null) {
                                acknowledge(purchase.purchaseToken) {
                                    continuation.resume(it)
                                }
                            } else {
                                continuation.resume(false)
                            }
                        }
                    }
                } else {
                    continuation.resume(false)
                }
                resumed = true
            }

            override fun onBillingServiceDisconnected() {
                if (!resumed) {
                    continuation.resume(false)
                }
            }
        })
    }
}
