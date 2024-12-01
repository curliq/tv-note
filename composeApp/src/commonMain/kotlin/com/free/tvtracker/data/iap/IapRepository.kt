package com.free.tvtracker.data.iap

import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import kotlinx.coroutines.flow.MutableStateFlow

class IapRepository(
    private val localDataSource: LocalSqlDataProvider,
    private val appPriceProvider: AppPriceProvider,
) {

    val isPurchased = MutableStateFlow(isPurchased())

    private fun isPurchased(): Boolean {
        val localPrefs = localDataSource.getLocalPreferences()
        return localPrefs.purchasedApp
    }

    suspend fun purchase(): Boolean {
        val success = appPriceProvider.buyApp()
        if (success) {
            setAppPurchased()
        }
        return success
    }

    fun setAppPurchased() {
        val localPrefs = localDataSource.getLocalPreferences()
        localDataSource.setLocalPreferences(localPrefs.copy(purchasedApp = true))
        isPurchased.value = true
    }

    suspend fun getPrice(): String? = appPriceProvider.appPrice()
}
