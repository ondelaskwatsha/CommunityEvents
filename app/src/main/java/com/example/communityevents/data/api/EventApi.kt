package com.example.communityevents.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface EventApi {
    @GET("discovery/v2/events.json")
    suspend fun searchEvents(
        @Query("apikey") apiKey: String,
        @Query("city") city: String? = null,
        @Query("countryCode") countryCode: String? = null,
        @Query("sort") sort: String = "date,asc",
        @Query("size") size: Int = 20
    ): TicketmasterResponse
}