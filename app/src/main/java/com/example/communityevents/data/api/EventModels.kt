package com.example.communityevents.data.api

import com.google.gson.annotations.SerializedName

data class TicketmasterResponse(
    @SerializedName("_embedded") val embedded: Embedded?
)

data class Embedded(
    @SerializedName("events") val events: List<EventDto>?
)

data class EventDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("images") val images: List<ImageDto>?,
    @SerializedName("dates") val dates: DatesDto?,
    @SerializedName("_embedded") val venueEmbed: VenueEmbed?
)

data class ImageDto(
    @SerializedName("url") val url: String
)

data class DatesDto(
    @SerializedName("start") val start: StartDto?
)

data class StartDto(
    @SerializedName("localDate") val localDate: String?
)

data class VenueEmbed(
    @SerializedName("venues") val venues: List<VenueDto>?
)

data class VenueDto(
    @SerializedName("name") val name: String?,
    @SerializedName("city") val city: CityDto?
)

data class CityDto(
    @SerializedName("name") val name: String?
)