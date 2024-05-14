package com.free.tvtracker.screens.details.mappers

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.screens.details.DetailsUiModel.WatchProvider
import com.free.tvtracker.utils.TmdbConfigData

class ShowWatchProviderUiModelMapper : Mapper<TmdbShowDetailsApiModel.WatchProvider, WatchProvider> {
    override fun map(from: TmdbShowDetailsApiModel.WatchProvider): WatchProvider {
        return WatchProvider(
            logo = TmdbConfigData.get().getLogoUrl(from.logoPath ?: ""),
            deeplink = from.providerName ?: ""
        )
    }
}
