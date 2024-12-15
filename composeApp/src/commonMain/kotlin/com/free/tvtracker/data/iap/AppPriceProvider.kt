package com.free.tvtracker.data.iap

interface AppPriceProvider {
    suspend fun appPrice(): String?
    suspend fun buyApp(): Boolean
    suspend fun restorePurchase(): Boolean
}
