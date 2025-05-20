package com.free.tvtracker.data.reviews

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RapidImdbReviewsResponse(
    val data: Data?,
)

@Serializable
data class Data(
    val title: Title?,
)

@Serializable
data class Title(
    @SerialName("__typename")
    val typename: String?,
    val id: String?,
    val titleText: TitleText?,
    val originalTitleText: OriginalTitleText?,
    val releaseYear: ReleaseYear?,
    val releaseDate: ReleaseDate?,
    val titleType: TitleType?,
    val primaryImage: PrimaryImage?,
    val reviews: Reviews?,
)

@Serializable
data class TitleText(
    val text: String?,
    val isOriginalTitle: Boolean?,
)

@Serializable
data class OriginalTitleText(
    val text: String?,
    val isOriginalTitle: Boolean?,
)

@Serializable
data class ReleaseYear(
    @SerialName("__typename")
    val typename: String?,
    val year: Long?,
    val endYear: Long?,
)

@Serializable
data class ReleaseDate(
    @SerialName("__typename")
    val typename: String?,
    val month: Long?,
    val day: Long?,
    val year: Long?,
    val country: Country?,
    val restriction: String?,
    val attributes: List<Attribute>?,
    val displayableProperty: DisplayableProperty?,
)

@Serializable
data class Country(
    val id: String?,
)

@Serializable
data class Attribute(
    val id: String?,
    val text: String?,
)

@Serializable
data class DisplayableProperty(
    val qualifiersInMarkdownList: String?,
)

@Serializable
data class TitleType(
    @SerialName("__typename")
    val typename: String?,
    val id: String?,
    val text: String?,
    val categories: List<Category>?,
    val canHaveEpisodes: Boolean?,
    val isEpisode: Boolean?,
    val isSeries: Boolean?,
    val displayableProperty: DisplayableProperty2?,
)

@Serializable
data class Category(
    val id: String?,
    val text: String?,
    val value: String?,
)

@Serializable
data class DisplayableProperty2(
    val value: Value?,
)

@Serializable
data class Value(
    val plainText: String?,
)

@Serializable
data class PrimaryImage(
    @SerialName("__typename")
    val typename: String?,
    val id: String?,
    val url: String?,
    val height: Long?,
    val width: Long?,
)

@Serializable
data class Reviews(
    val pageInfo: PageInfo?,
    val total: Long?,
    val edges: List<Edge>?,
)

@Serializable
data class PageInfo(
    @SerialName("__typename")
    val typename: String?,
    val hasNextPage: Boolean?,
    val hasPreviousPage: Boolean?,
    val startCursor: String?,
    val endCursor: String?,
)

@Serializable
data class Edge(
    val node: Node?,
)

@Serializable
data class Node(
    @SerialName("__typename")
    val typename: String?,
    val id: String?,
    val title: Title2?,
    val author: Author?,
    val authorRating: Long?,
    val helpfulness: Helpfulness?,
    val summary: Summary?,
    val text: Text?,
    val language: String?,
    val submissionDate: String?,
    val spoiler: Boolean?,
)

@Serializable
data class Title2(
    val id: String?,
)

@Serializable
data class Author(
    val id: String?,
    val nickName: String?,
)

@Serializable
data class Helpfulness(
    @SerialName("__typename")
    val typename: String?,
    val upVotes: Long?,
    val downVotes: Long?,
    val score: Double?,
)

@Serializable
data class Summary(
    val originalText: String?,
)

@Serializable
data class Text(
    val originalText: OriginalText?,
)

@Serializable
data class OriginalText(
    val plainText: String?,
)
