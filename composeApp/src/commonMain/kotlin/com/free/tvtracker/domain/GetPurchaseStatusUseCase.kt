package com.free.tvtracker.domain

import com.free.tvtracker.core.Logger
import com.free.tvtracker.data.iap.IapRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GetPurchaseStatusUseCase(
    private val iapRepository: IapRepository,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val logger: Logger
) {
    private val appPrice = MutableStateFlow("$0.97")
    private val subPrice = MutableStateFlow("$2.97")

    @OptIn(DelicateCoroutinesApi::class)
    fun invoke(): Flow<PurchaseStatus> {
        GlobalScope.launch {
            appPrice.value = iapRepository.getPrice()
            subPrice.value = iapRepository.getSubPrice()
        }
        return combine(
            trackedShowsRepository.allShows,
            iapRepository.isPurchased,
            iapRepository.isHacked,
            appPrice,
            subPrice,
        ) { shows, purchased, hacked, appPrice, subPrice ->
            val status = if (purchased || hacked) {
                PurchaseStatus.Status.Purchased
            } else {
                if (shows.isEmpty()) {
                    PurchaseStatus.Status.TrialOn
                } else {
                    PurchaseStatus.Status.TrialFinished
                }
            }
            PurchaseStatus(
                status = status,
                price = appPrice,
                subPrice = subPrice
            )
        }
    }
}

data class PurchaseStatus(val status: Status, val price: String, val subPrice: String) {
    enum class Status {
        Purchased, TrialOn, TrialFinished
    }
}
