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
        return trackedShowsRepository.allShows.combine(iapRepository.isPurchased) { shows, purchased ->
            if (purchased) {
                PurchaseStatus.Status.Purchased
            } else {
                if (shows.isEmpty()) {
                    PurchaseStatus.Status.TrialOn
                } else if (shows.isNotEmpty()) {
                    PurchaseStatus.Status.TrialFinished
                } else {
                    PurchaseStatus.Status.Purchased
                }
            }
        }.map {
            PurchaseStatus(
                status = it,
                price = iapRepository.getPrice() ?: "",
                subPrice = iapRepository.getSubPrice() ?: ""
            )
        }
    }
}

data class PurchaseStatus(val status: Status, val price: String, val subPrice: String) {
    enum class Status {
        Purchased, TrialOn, TrialFinished
    }
}
