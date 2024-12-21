package com.free.tvtracker.data.iap

interface AppPriceProvider {
    suspend fun appPrice(): String?
    suspend fun appSubPrice(): String?
    suspend fun buyApp(): Boolean
    suspend fun subscribe(): Boolean
    suspend fun restorePurchase(): Boolean
}
