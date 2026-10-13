package com.example.communityevents

// =====================================================================
// Event.kt
// This file holds our DATA: what an "event" is, and a list of events.
// No design code lives here — only information.
// =====================================================================

// A "data class" is like a form with blanks to fill in.
// Every event in the app has these details:
data class Event(
    val id: Int,           // a unique number so we can tell events apart
    val title: String,     // e.g. "Music in the Park"
    val category: String,  // "Outdoors", "Music" or "Family" (used by the filter chips)
    val month: String,     // short month shown on the picture, e.g. "JUN"
    val day: String,       // day number shown on the picture, e.g. "22"
    val time: String,      // e.g. "4:00 pm – 7:00 pm"
    val place: String,     // e.g. "Willow Park Bandstand"
    val imageRes: Int      // the picture, taken from the res/drawable folder
)

// Our list of events. Later you could load these from a database or an API,
// but for now we simply type them in by hand.
val sampleEvents = listOf(
    Event(
        id = 1,
        title = "Community Garden Day",
        category = "Outdoors",
        month = "JUN",
        day = "14",
        time = "10:00 am – 1:00 pm",
        place = "Riverside Community Garden",
        imageRes = R.drawable.garden   // the file res/drawable/garden.jpg
    ),
    Event(
        id = 2,
        title = "Music in the Park",
        category = "Music",
        month = "JUN",
        day = "22",
        time = "4:00 pm – 7:00 pm",
        place = "Willow Park Bandstand",
        imageRes = R.drawable.park     // the file res/drawable/park.jpg
    ),
    Event(
        id = 3,
        title = "Family Art Workshop",
        category = "Family",
        month = "JUN",
        day = "28",
        time = "11:00 am – 12:30 pm",
        place = "Northside Library",
        imageRes = R.drawable.art      // the file res/drawable/art.jpg
    )
)