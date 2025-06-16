package com.free.tvtracker.ui.details.mappers

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel
import com.free.tvtracker.constants.TmdbVideoSite
import com.free.tvtracker.ui.details.DetailsUiModel.Video

class ContentShowVideoUiModelMapper : Mapper<TmdbShowDetailsApiModel.Video, Video?> {
    override fun map(from: TmdbShowDetailsApiModel.Video): Video? {
        return when (TmdbVideoSite.entries.firstOrNull { it.site == from.site }) {
            TmdbVideoSite.YOUTUBE -> {
                Video(
                    thumbnail = "https://img.youtube.com/vi/${from.key}/0.jpg",
                    videoUrl = "https://www.youtube.com/watch?v=${from.key}",
                    title = from.name
                )
            }

            null -> null
        }
    }
}
