package com.free.tvtracker.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel

data class TmdbShowWatchProvidersResponse(
    @JsonProperty("results") var results: Map<String, Providers>? = null
)

data class Results(
    @JsonProperty("AD")
    val ad: Providers? = null,
    @JsonProperty("AL")
    val al: Providers? = null,
    @JsonProperty("AR")
    val ar: Providers? = null,
    @JsonProperty("AT")
    val at: Providers? = null,
    @JsonProperty("AU")
    val au: Providers? = null,
    @JsonProperty("BA")
    val ba: Providers? = null,
    @JsonProperty("BE")
    val be: Providers? = null,
    @JsonProperty("BG")
    val bg: Providers? = null,
    @JsonProperty("BO")
    val bo: Providers? = null,
    @JsonProperty("BR")
    val br: Providers? = null,
    @JsonProperty("BZ")
    val bz: Providers? = null,
    @JsonProperty("CA")
    val ca: Providers? = null,
    @JsonProperty("CH")
    val ch: Providers? = null,
    @JsonProperty("CL")
    val cl: Providers? = null,
    @JsonProperty("CO")
    val co: Providers? = null,
    @JsonProperty("CR")
    val cr: Providers? = null,
    @JsonProperty("CZ")
    val cz: Providers? = null,
    @JsonProperty("DE")
    val de: Providers? = null,
    @JsonProperty("DK")
    val dk: Providers? = null,
    @JsonProperty("DO")
    val do_field: Providers? = null,
    @JsonProperty("EC")
    val ec: Providers? = null,
    @JsonProperty("EE")
    val ee: Providers? = null,
    @JsonProperty("EG")
    val eg: Providers? = null,
    @JsonProperty("ES")
    val es: Providers? = null,
    @JsonProperty("FI")
    val fi: Providers? = null,
    @JsonProperty("FR")
    val fr: Providers? = null,
    @JsonProperty("GB")
    val gb: Providers? = null,
    @JsonProperty("GR")
    val gr: Providers? = null,
    @JsonProperty("GT")
    val gt: Providers? = null,
    @JsonProperty("HK")
    val hk: Providers? = null,
    @JsonProperty("HN")
    val hn: Providers? = null,
    @JsonProperty("HR")
    val hr: Providers? = null,
    @JsonProperty("HU")
    val hu: Providers? = null,
    @JsonProperty("ID")
    val id: Providers? = null,
    @JsonProperty("IE")
    val ie: Providers? = null,
    @JsonProperty("IN")
    val in_field: Providers? = null,
    @JsonProperty("IS")
    val is_field: Providers? = null,
    @JsonProperty("IT")
    val it: Providers? = null,
    @JsonProperty("JM")
    val jm: Providers? = null,
    @JsonProperty("JP")
    val jp: Providers? = null,
    @JsonProperty("KR")
    val kr: Providers? = null,
    @JsonProperty("LC")
    val lc: Providers? = null,
    @JsonProperty("LI")
    val li: Providers? = null,
    @JsonProperty("LT")
    val lt: Providers? = null,
    @JsonProperty("LU")
    val lu: Providers? = null,
    @JsonProperty("LV")
    val lv: Providers? = null,
    @JsonProperty("ME")
    val me: Providers? = null,
    @JsonProperty("MK")
    val mk: Providers? = null,
    @JsonProperty("MT")
    val mt: Providers? = null,
    @JsonProperty("MX")
    val mx: Providers? = null,
    @JsonProperty("MY")
    val my: Providers? = null,
    @JsonProperty("NI")
    val ni: Providers? = null,
    @JsonProperty("NL")
    val nl: Providers? = null,
    @JsonProperty("NO")
    val no: Providers? = null,
    @JsonProperty("NZ")
    val nz: Providers? = null,
    @JsonProperty("PA")
    val pa: Providers? = null,
    @JsonProperty("PE")
    val pe: Providers? = null,
    @JsonProperty("PH")
    val ph: Providers? = null,
    @JsonProperty("PL")
    val pl: Providers? = null,
    @JsonProperty("PT")
    val pt: Providers? = null,
    @JsonProperty("PY")
    val py: Providers? = null,
    @JsonProperty("RO")
    val ro: Providers? = null,
    @JsonProperty("RS")
    val rs: Providers? = null,
    @JsonProperty("SE")
    val se: Providers? = null,
    @JsonProperty("SG")
    val sg: Providers? = null,
    @JsonProperty("SI")
    val si: Providers? = null,
    @JsonProperty("SK")
    val sk: Providers? = null,
    @JsonProperty("SM")
    val sm: Providers? = null,
    @JsonProperty("SV")
    val sv: Providers? = null,
    @JsonProperty("TH")
    val th: Providers? = null,
    @JsonProperty("TR")
    val tr: Providers? = null,
    @JsonProperty("TT")
    val tt: Providers? = null,
    @JsonProperty("TW")
    val tw: Providers? = null,
    @JsonProperty("US")
    val us: Providers? = null,
    @JsonProperty("UY")
    val uy: Providers? = null,
    @JsonProperty("VE")
    val ve: Providers? = null,
)

data class Flatrate(
    @JsonProperty("logo_path")
    val logoPath: String? = null,
    @JsonProperty("provider_id")
    val providerId: Long,
    @JsonProperty("provider_name")
    val providerName: String? = null,
    @JsonProperty("display_priority")
    val displayPriority: Long,
) {
    fun toApiModel(): TmdbShowDetailsApiModel.WatchProvider {
        return TmdbShowDetailsApiModel.WatchProvider(
            logoPath = this.logoPath,
            providerId = this.providerId.toInt(),
            providerName = this.providerName,
            displayPriority = this.displayPriority.toInt(),
        )
    }
}

data class Free(
    @JsonProperty("logo_path")
    val logoPath: String? = null,
    @JsonProperty("provider_id")
    val providerId: Long,
    @JsonProperty("provider_name")
    val providerName: String? = null,
    @JsonProperty("display_priority")
    val displayPriority: Long,
)

data class Providers(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("free") val free: List<Free>? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)
