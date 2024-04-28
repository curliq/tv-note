package com.free.tvtracker.core.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel

data class TmdbShowWatchProvidersResponse(
    @JsonProperty("results") var results: Results? = null
)

data class Results(
    @JsonProperty("AD")
    val ad: Ad? = null,
    @JsonProperty("AL")
    val al: Al? = null,
    @JsonProperty("AR")
    val ar: Ar? = null,
    @JsonProperty("AT")
    val at: At? = null,
    @JsonProperty("AU")
    val au: Au? = null,
    @JsonProperty("BA")
    val ba: Ba? = null,
    @JsonProperty("BE")
    val be: Be? = null,
    @JsonProperty("BG")
    val bg: Bg? = null,
    @JsonProperty("BO")
    val bo: Bo? = null,
    @JsonProperty("BR")
    val br: Br? = null,
    @JsonProperty("BZ")
    val bz: Bz? = null,
    @JsonProperty("CA")
    val ca: Ca? = null,
    @JsonProperty("CH")
    val ch: Ch? = null,
    @JsonProperty("CL")
    val cl: Cl? = null,
    @JsonProperty("CO")
    val co: Co? = null,
    @JsonProperty("CR")
    val cr: Cr? = null,
    @JsonProperty("CZ")
    val cz: Cz? = null,
    @JsonProperty("DE")
    val de: De? = null,
    @JsonProperty("DK")
    val dk: Dk? = null,
    @JsonProperty("DO")
    val do_field: Do? = null,
    @JsonProperty("EC")
    val ec: Ec? = null,
    @JsonProperty("EE")
    val ee: Ee? = null,
    @JsonProperty("EG")
    val eg: Eg? = null,
    @JsonProperty("ES")
    val es: Es? = null,
    @JsonProperty("FI")
    val fi: Fi? = null,
    @JsonProperty("FR")
    val fr: Fr? = null,
    @JsonProperty("GB")
    val gb: Gb? = null,
    @JsonProperty("GR")
    val gr: Gr? = null,
    @JsonProperty("GT")
    val gt: Gt? = null,
    @JsonProperty("HK")
    val hk: Hk? = null,
    @JsonProperty("HN")
    val hn: Hn? = null,
    @JsonProperty("HR")
    val hr: Hr? = null,
    @JsonProperty("HU")
    val hu: Hu? = null,
    @JsonProperty("ID")
    val id: Id? = null,
    @JsonProperty("IE")
    val ie: Ie? = null,
    @JsonProperty("IN")
    val in_field: In? = null,
    @JsonProperty("IS")
    val is_field: Is? = null,
    @JsonProperty("IT")
    val it: It? = null,
    @JsonProperty("JM")
    val jm: Jm? = null,
    @JsonProperty("JP")
    val jp: Jp? = null,
    @JsonProperty("KR")
    val kr: Kr? = null,
    @JsonProperty("LC")
    val lc: Lc? = null,
    @JsonProperty("LI")
    val li: Li? = null,
    @JsonProperty("LT")
    val lt: Lt? = null,
    @JsonProperty("LU")
    val lu: Lu? = null,
    @JsonProperty("LV")
    val lv: Lv? = null,
    @JsonProperty("ME")
    val me: Me? = null,
    @JsonProperty("MK")
    val mk: Mk? = null,
    @JsonProperty("MT")
    val mt: Mt? = null,
    @JsonProperty("MX")
    val mx: Mx? = null,
    @JsonProperty("MY")
    val my: My? = null,
    @JsonProperty("NI")
    val ni: Ni? = null,
    @JsonProperty("NL")
    val nl: Nl? = null,
    @JsonProperty("NO")
    val no: No? = null,
    @JsonProperty("NZ")
    val nz: Nz? = null,
    @JsonProperty("PA")
    val pa: Pa? = null,
    @JsonProperty("PE")
    val pe: Pe? = null,
    @JsonProperty("PH")
    val ph: Ph? = null,
    @JsonProperty("PL")
    val pl: Pl? = null,
    @JsonProperty("PT")
    val pt: Pt? = null,
    @JsonProperty("PY")
    val py: Py? = null,
    @JsonProperty("RO")
    val ro: Ro? = null,
    @JsonProperty("RS")
    val rs: Rs? = null,
    @JsonProperty("SE")
    val se: Se? = null,
    @JsonProperty("SG")
    val sg: Sg? = null,
    @JsonProperty("SI")
    val si: Si? = null,
    @JsonProperty("SK")
    val sk: Sk? = null,
    @JsonProperty("SM")
    val sm: Sm? = null,
    @JsonProperty("SV")
    val sv: Sv? = null,
    @JsonProperty("TH")
    val th: Th? = null,
    @JsonProperty("TR")
    val tr: Tr? = null,
    @JsonProperty("TT")
    val tt: Tt? = null,
    @JsonProperty("TW")
    val tw: Tw? = null,
    @JsonProperty("US")
    val us: Us? = null,
    @JsonProperty("UY")
    val uy: Uy? = null,
    @JsonProperty("VE")
    val ve: Ve? = null,
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

data class Ad(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Al(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Ar(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class At(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Au(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Ba(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Be(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Bg(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Bo(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Br(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Bz(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Ca(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Ch(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Cl(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Co(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Cr(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Cz(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class De(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Dk(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Do(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Ec(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Ee(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Eg(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Es(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Fi(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Fr(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Gb(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Gr(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Gt(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Hk(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Hn(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Hr(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Hu(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Id(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Ie(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class In(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Is(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class It(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Jm(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Jp(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Kr(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Lc(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Li(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Lt(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Lu(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Lv(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Me(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Mk(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Mt(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Mx(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class My(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Ni(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Nl(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class No(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Nz(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Pa(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Pe(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Ph(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Pl(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Pt(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Py(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Ro(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Rs(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Se(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Sg(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Si(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Sk(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Sm(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Sv(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Th(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Tr(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Tt(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Tw(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Us(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("free") val free: List<Free>? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Uy(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)

data class Ve(
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("flatrate") val flatrate: List<Flatrate>? = null,
)
