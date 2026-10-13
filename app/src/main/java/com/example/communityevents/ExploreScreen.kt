package com.example.communityevents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =====================================================================
// ExploreScreen.kt
// This file draws the whole "Explore" screen you see in the design.
//
// Think of it like LEGO: we build small pieces (header, search box,
// cards...) and then stack them together inside ExploreScreen().
// =====================================================================


// ---------------------------------------------------------------------
// 1. COLOURS
// We save every colour once here, so we never have to remember codes.
// 0xFF at the start just means "fully visible (not see-through)".
// ---------------------------------------------------------------------
val DarkGreen = Color(0xFF1C3D36)   // main headings, "All" chip
val Orange    = Color(0xFFD9623B)   // "near you?", add button, badge
val Rust      = Color(0xFFB0532F)   // small labels like "OUTDOORS"
val Cream     = Color(0xFFFCFBF6)   // page background
val GreyText  = Color(0xFF6B7672)   // softer text (dates, places)
val BorderGrey = Color(0xFFE3E3DE)  // thin lines around boxes
val SunYellow = Color(0xFFF2C94C)   // "FREE EVENT" tag
val SoftPink  = Color(0xFFFBE7E1)   // "POPULAR" tag background
val Peach     = Color(0xFFE89F7E)   // profile circle
val HeartBg   = Color(0xFFF4EEE7)   // circle behind the heart


// ---------------------------------------------------------------------
// 2. THE MAIN SCREEN
// This puts every piece together, top to bottom.
// ---------------------------------------------------------------------
@Composable
fun ExploreScreen() {

    // "State" = things the screen must REMEMBER while you use it.
    // When state changes, Compose redraws the screen automatically.
    var searchText by remember { mutableStateOf("") }          // what's typed in the search box
    var selectedCategory by remember { mutableStateOf("All") } // which chip is chosen
    val savedIds = remember { mutableStateListOf(2) }          // ids of hearted events (Music in the Park starts saved)

    // Work out which events to show, using the chip AND the search box.
    val visibleEvents = sampleEvents.filter { event ->
        val categoryMatches = selectedCategory == "All" || event.category == selectedCategory
        val searchMatches = event.title.contains(searchText, ignoreCase = true) ||
                event.place.contains(searchText, ignoreCase = true)
        categoryMatches && searchMatches   // show it only if BOTH are true
    }

    // The event shown in the big "Don't miss this" card.
    val featuredEvent = sampleEvents.first { it.id == 2 }

    // Scaffold = a ready-made page layout. We give it a bottom bar,
    // and it makes sure our content doesn't hide behind that bar.
    Scaffold(
        containerColor = Cream,
        bottomBar = { BottomBar(savedCount = savedIds.size) }
    ) { innerPadding ->

        // LazyColumn = a list you can scroll up and down.
        // (Your 2 screenshots are really ONE screen — the 2nd is just scrolled down.)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item { TopHeader() }
            item { HeadlineSection() }
            item { SearchBox(text = searchText, onTextChange = { searchText = it }) }
            item { CategoryChips(selected = selectedCategory, onSelect = { selectedCategory = it }) }

            item {
                SectionTitle(smallLabel = "FEATURED THIS WEEK", bigTitle = "Don’t miss this") {
                    PopularBadge()
                }
            }
            item { FeaturedCard(event = featuredEvent, dateText = "Sunday, 22 June · 4:00 pm") }

            item {
                SectionTitle(smallLabel = "AROUND RIVERSIDE", bigTitle = "Coming up") {
                    Text(
                        text = "${visibleEvents.size} events",
                        color = GreyText,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Draw one EventCard for every event in our filtered list.
            items(visibleEvents, key = { it.id }) { event ->
                EventCard(
                    event = event,
                    isSaved = event.id in savedIds,
                    onHeartClick = {
                        // Tap once = save, tap again = unsave
                        if (event.id in savedIds) savedIds.remove(event.id)
                        else savedIds.add(event.id)
                    }
                )
            }

            // Friendly message if nothing matches the search
            if (visibleEvents.isEmpty()) {
                item {
                    Text(
                        text = "No events match your search.",
                        color = GreyText,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }

            item { Spacer(Modifier.height(24.dp)) } // a little space at the very bottom
        }
    }
}


// ---------------------------------------------------------------------
// 3. TOP HEADER  — "YOUR COMMUNITY / Riverside" + profile circle
// ---------------------------------------------------------------------
@Composable
fun TopHeader() {
    // Row = place things side by side (left to right)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Green rounded square with the "people" icon
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(DarkGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Groups, contentDescription = null, tint = Color.White)
        }

        Spacer(Modifier.width(12.dp))

        // Column = place things on top of each other (top to bottom)
        // weight(1f) = "take up all the leftover space", which pushes the
        // profile circle all the way to the right.
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "YOUR COMMUNITY",
                fontSize = 12.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.SemiBold,
                color = GreyText
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Riverside", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Outlined.Place, contentDescription = null, tint = Orange, modifier = Modifier.size(18.dp))
            }
        }

        // Profile circle with a small green "online" dot.
        // Box = place things ON TOP of each other (like layers).
        Box {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Peach)
                    .border(3.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("O", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Rust)
            }
            // The green dot sits in the bottom-right corner
            Box(
                modifier = Modifier
                    .size(13.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
                    .border(2.dp, Color.White, CircleShape)
            )
        }
    }
    HorizontalDivider(color = BorderGrey) // thin line under the header
}


// ---------------------------------------------------------------------
// 4. HEADLINE — date + "What's happening near you?"
// ---------------------------------------------------------------------
@Composable
fun HeadlineSection() {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
        // The date is typed in by hand to match the design.
        Text("Saturday, 7 June", color = GreyText, fontSize = 15.sp)
        Spacer(Modifier.height(6.dp))
        Text(
            text = "What’s happening",
            fontFamily = FontFamily.Serif,   // the "book-style" font from the design
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            lineHeight = 38.sp,
            color = DarkGreen
        )
        Text(
            text = "near you?",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            color = Orange
        )
    }
}


// ---------------------------------------------------------------------
// 5. SEARCH BOX
// "text" is what's inside the box; "onTextChange" runs every time you type.
// ---------------------------------------------------------------------
@Composable
fun SearchBox(text: String, onTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        placeholder = { Text("Search events or places", color = GreyText) },
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search", tint = GreyText) },
        singleLine = true,
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkGreen,
            unfocusedBorderColor = BorderGrey,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )
}


// ---------------------------------------------------------------------
// 6. CATEGORY CHIPS — All / Outdoors / Music / Family
// ---------------------------------------------------------------------
@Composable
fun CategoryChips(selected: String, onSelect: (String) -> Unit) {
    val categories = listOf("All", "Outdoors", "Music", "Family")

    // horizontalScroll lets you swipe sideways if the chips don't fit
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(start = 20.dp, end = 20.dp, top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (category in categories) {
            val isSelected = category == selected

            Text(
                text = category,
                fontWeight = FontWeight.SemiBold,
                // Selected chip = white text on green. Others = grey text on white.
                color = if (isSelected) Color.White else GreyText,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))     // 50 = fully round ends
                    .background(if (isSelected) DarkGreen else Color.White)
                    .border(1.dp, if (isSelected) DarkGreen else BorderGrey, RoundedCornerShape(50))
                    .clickable { onSelect(category) } // tell the main screen which chip was tapped
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}


// ---------------------------------------------------------------------
// 7. SECTION TITLE — small coloured label + big title, with something
// on the right side (the "POPULAR" tag or the "3 events" text).
// "rightSide" is a small piece of screen we pass in from outside.
// ---------------------------------------------------------------------
@Composable
fun SectionTitle(smallLabel: String, bigTitle: String, rightSide: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 14.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(smallLabel, color = Rust, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text(
                text = bigTitle,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = DarkGreen
            )
        }
        rightSide()
    }
}

// The small pink "POPULAR" tag
@Composable
fun PopularBadge() {
    Text(
        text = "POPULAR",
        color = Rust,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(SoftPink)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    )
}


// ---------------------------------------------------------------------
// 8. FEATURED CARD — the big photo card "Music in the Park"
// ---------------------------------------------------------------------
@Composable
fun FeaturedCard(event: Event, dateText: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(26.dp))
    ) {
        // Layer 1: the photo, cropped to fill the card
        Image(
            painter = painterResource(event.imageRes),
            contentDescription = event.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Layer 2: a dark fade at the bottom so the white text is easy to read
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)))
                )
        )

        // Layer 3: the text, stuck to the bottom-left corner
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(
                text = "FREE EVENT",
                color = DarkGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(SunYellow)
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = event.title,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.White
            )
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(dateText, color = Color.White, fontSize = 15.sp)
            }
        }
    }
}


// ---------------------------------------------------------------------
// 9. EVENT CARD — one white card in the "Coming up" list
// ---------------------------------------------------------------------
@Composable
fun EventCard(event: Event, isSaved: Boolean, onHeartClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(1.dp, BorderGrey, RoundedCornerShape(24.dp))
            .padding(12.dp)
    ) {
        // LEFT: photo with the little date label on top of it
        Box(
            modifier = Modifier
                .size(width = 110.dp, height = 124.dp)
                .clip(RoundedCornerShape(18.dp))
        ) {
            Image(
                painter = painterResource(event.imageRes),
                contentDescription = event.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // White date label: "JUN / 14"
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(event.month, color = Rust, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(event.day, color = DarkGreen, fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
            }
        }

        Spacer(Modifier.width(14.dp))

        // MIDDLE: category, title, time and place
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = event.category.uppercase(),
                color = Rust,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = event.title,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = DarkGreen
            )
            Spacer(Modifier.height(6.dp))
            InfoRow(icon = Icons.Outlined.Schedule, text = event.time)
            InfoRow(icon = Icons.Outlined.Place, text = event.place)
        }

        // RIGHT: the heart button
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(HeartBg)
                .clickable { onHeartClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                // Filled heart if saved, outline heart if not
                imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isSaved) "Remove from saved" else "Save event",
                tint = Rust
            )
        }
    }
}

// A small icon + text line (used for time and place)
@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = GreyText, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(text, color = GreyText, fontSize = 14.sp)
    }
}


// ---------------------------------------------------------------------
// 10. BOTTOM BAR — Explore / Saved / Add event / Profile
// ---------------------------------------------------------------------
@Composable
fun BottomBar(
    savedCount: Int,
    selected: String = "Explore",
    onExploreClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Column(modifier = Modifier.background(Cream)) {
        HorizontalDivider(color = BorderGrey)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()        // keeps it above the phone's own buttons
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Explore (orange = the page we're on if selected)
            NavItem(
                icon = Icons.Outlined.Home,
                label = "Explore",
                color = if (selected == "Explore") Orange else GreyText,
                onClick = onExploreClick
            )

            // Saved, with a little number bubble showing how many hearts
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BadgedBox(
                    badge = {
                        if (savedCount > 0) {
                            Badge(containerColor = Orange, contentColor = Color.White) {
                                Text("$savedCount")
                            }
                        }
                    }
                ) {
                    Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Saved", tint = GreyText)
                }
                Text("Saved", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = GreyText)
            }

            // Big orange "+" button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Orange),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add event", tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Text("Add event", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
            }

            // Profile
            NavItem(
                icon = Icons.Outlined.PersonOutline,
                label = "Profile",
                color = if (selected == "Profile") Orange else GreyText,
                onClick = onProfileClick
            )
        }
    }
}

// One simple icon + label for the bottom bar
@Composable
fun NavItem(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(icon, contentDescription = label, tint = color)
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = color)
    }
}