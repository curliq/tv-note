package com.free.tvtracker.data.iap

import com.free.tvtracker.data.common.LocalSqlDataProvider
import kotlinx.coroutines.flow.MutableStateFlow

class IapRepository(
    private val localDataSource: LocalSqlDataProvider,
    private val appPriceProvider: AppPriceProvider,
) {

    val isPurchased = MutableStateFlow(isPurchased())
    val isHacked = MutableStateFlow(isHacked())

    private fun isPurchased(): Boolean {
        val localPrefs = localDataSource.getLocalPreferences()
        return localPrefs.purchasedApp
    }

    private fun isHacked(): Boolean {
        val localPrefs = localDataSource.getLocalPreferences()
        return localPrefs.isHacked
    }

    fun setHacked() {
        val localPrefs = localDataSource.getLocalPreferences()
        localDataSource.setLocalPreferences(localPrefs.copy(isHacked = true))
        isHacked.value = true
    }

    suspend fun purchase(): Boolean {
        val success = appPriceProvider.buyApp()
        setAppPurchased(owned = success)
        return success
    }

    suspend fun subscribe(): Boolean {
        val success = appPriceProvider.subscribe()
        setAppPurchased(owned = success)
        return success
    }

    private fun setAppPurchased(owned: Boolean) {
        val localPrefs = localDataSource.getLocalPreferences()
        localDataSource.setLocalPreferences(localPrefs.copy(purchasedApp = owned))
        isPurchased.value = owned
    }

    suspend fun getPrice(): String = appPriceProvider.appPrice() ?: "$2.98"

    suspend fun restorePurchase(): Boolean {
        val success = appPriceProvider.restorePurchase()
        setAppPurchased(owned = success)
        return success
    }

    suspend fun getSubPrice(): String = appPriceProvider.appSubPrice()  ?: "$0.98"
}
