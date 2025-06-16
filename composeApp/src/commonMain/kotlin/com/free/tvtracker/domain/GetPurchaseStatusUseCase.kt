package com.free.tvtracker.domain

import com.free.tvtracker.data.iap.IapRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GetPurchaseStatusUseCase(
    private val iapRepository: IapRepository,
    private val trackedShowsRepository: TrackedShowsRepository
) {
    fun invoke(): Flow<PurchaseStatus> {
        return combine(
            trackedShowsRepository.allShows,
            iapRepository.isPurchased,
            iapRepository.isHacked
        ) { shows, purchased, hacked ->
            if (purchased || hacked) {
                PurchaseStatus.Status.Purchased
            } else {
                if (shows.isEmpty()) {
                    PurchaseStatus.Status.TrialOn
                } else {
                    PurchaseStatus.Status.TrialFinished
                }
            }
        }.map {
            PurchaseStatus(
                status = it,
                price = iapRepository.getPrice(),
                subPrice = iapRepository.getSubPrice()
            )
        }
    }
}

data class PurchaseStatus(val status: Status, val price: String, val subPrice: String) {
    enum class Status {
        Purchased, TrialOn, TrialFinished
    }
}
