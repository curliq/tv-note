package com.free.tvtracker.ui.details.mappers

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.ui.details.DetailsUiModel.WatchProvider
import com.free.tvtracker.ui.common.TmdbConfigData

class ShowWatchProviderUiModelMapper : Mapper<TmdbShowDetailsApiModel.WatchProvider, WatchProvider> {
    override fun map(from: TmdbShowDetailsApiModel.WatchProvider): WatchProvider {
        return WatchProvider(
            logo = TmdbConfigData.get().getLogoUrl(from.logoPath ?: ""),
            deeplink = from.providerName ?: ""
        )
    }
}
