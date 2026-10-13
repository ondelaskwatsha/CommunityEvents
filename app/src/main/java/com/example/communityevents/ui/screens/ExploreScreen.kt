package com.example.communityevents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.communityevents.BuildConfig
import com.example.communityevents.data.api.EventDto
import com.example.communityevents.data.api.RetrofitClient
import com.example.communityevents.ui.theme.DarkText
import com.example.communityevents.ui.theme.ForestGreen
import com.example.communityevents.ui.theme.OffWhite
import com.example.communityevents.ui.theme.Terracotta
import com.example.communityevents.ui.theme.WarmGray

@Composable
fun ExploreScreen() {
    var events by remember { mutableStateOf<List<EventDto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var reloadKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(reloadKey) {
        loading = true
        error = null

        if (BuildConfig.TICKETMASTER_API_KEY.isBlank()) {
            error = "Ticketmaster API key is missing. Add TICKETMASTER_API_KEY to local.properties and rebuild."
            loading = false
            return@LaunchedEffect
        }

        try {
            val response = RetrofitClient.api.searchEvents(
                apiKey = BuildConfig.TICKETMASTER_API_KEY,
                city = "Johannesburg",
                countryCode = "ZA",
                size = 20
            )
            events = response.embedded?.events ?: emptyList()
        } catch (e: retrofit2.HttpException) {
            error = when (e.code()) {
                401 -> "The API key was rejected (401). Check TICKETMASTER_API_KEY in local.properties."
                429 -> "Too many requests (429). Please wait a moment and retry."
                else -> "Server error (${e.code()}). Please retry."
            }
        } catch (_: java.io.IOException) {
            error = "No internet connection. Check your network and retry."
        } catch (e: Exception) {
            error = e.message ?: "Failed to load events"
        } finally {
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
            .padding(16.dp)
    ) {
        Text(
            text = "Community Events",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreen
        )
        Text(
            text = "Live from Ticketmaster API",
            fontSize = 13.sp,
            color = WarmGray
        )
        Spacer(Modifier.height(16.dp))

        when {
            loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Terracotta)
                }
            }
            error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = error ?: "",
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { reloadKey++ },
                            colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
            events.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No events found")
                }
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(events) { event ->
                        EventCard(event)
                    }
                }
            }
        }
    }
}

@Composable
private fun EventCard(event: EventDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            event.images?.firstOrNull()?.url?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = event.name,
                    modifier = Modifier
                        .size(80.dp)
                        .background(WarmGray, RoundedCornerShape(12.dp))
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = event.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DarkText,
                    maxLines = 2
                )
                Spacer(Modifier.height(4.dp))
                event.dates?.start?.localDate?.let {
                    Text(it, fontSize = 12.sp, color = WarmGray)
                }
                event.venueEmbed?.venues?.firstOrNull()?.let { venue ->
                    val cityName = venue.city?.name ?: ""
                    Text(
                        text = "${venue.name ?: ""} · $cityName",
                        fontSize = 12.sp,
                        color = WarmGray,
                        maxLines = 1
                    )
                }
            }
        }
    }
}