package com.example.communityevents



import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.provider.Settings
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TileBackground = Color(0xFFEFF4F1)
private val BeigeCard = Color(0xFFF5EEE2)
private val SignOutBackground = Color(0xFFFFF3EF)
private val SignOutBorder = Color(0xFFE9B9AB)
private val SwitchOffTrack = Color(0xFFCBD5D0)

@Composable
fun ProfileScreen(
    profile: UserProfile,
    savedCount: Int,
    onProfileChange: (UserProfile) -> Unit,
    onExploreClick: () -> Unit,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    val latestProfile by rememberUpdatedState(profile)
    val latestOnChange by rememberUpdatedState(onProfileChange)

    var showDetailsDialog by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var detecting by remember { mutableStateOf(false) }

    val detectLocation: () -> Unit = {
        detecting = true
        fetchCurrentPlace(context) { place ->
            detecting = false
            if (place != null) {
                latestOnChange(latestProfile.copy(location = place))
            } else {
                Toast.makeText(
                    context,
                    "Couldn't find your location. Check that location is turned on, or type it in.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        if (results.values.any { it }) {
            detectLocation()
        } else {
            Toast.makeText(
                context,
                "Location permission was denied. You can type your location instead.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val useCurrentLocation: () -> Unit = {
        if (hasLocationPermission(context)) {
            detectLocation()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        if (profile.location.isBlank()) useCurrentLocation()
    }

    Scaffold(
        containerColor = Cream,
        bottomBar = {
            BottomBar(
                savedCount = savedCount,
                selected = "Profile",
                onExploreClick = onExploreClick,
                onProfileClick = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "YOUR ACCOUNT",
                color = Rust,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Text(
                text = "Profile",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = DarkGreen
            )

            Spacer(Modifier.height(20.dp))

            val initials = initialsOf(profile.name)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color.White)
                    .border(1.dp, BorderGrey, RoundedCornerShape(26.dp))
                    .clickable { showDetailsDialog = true }
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Peach),
                    contentAlignment = Alignment.Center
                ) {
                    if (initials.isNotEmpty()) {
                        Text(
                            text = initials,
                            color = Rust,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.PersonOutline,
                            contentDescription = null,
                            tint = Rust,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(Modifier.width(18.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile.name.ifBlank { "Add your name" },
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = if (profile.name.isBlank()) GreyText else DarkGreen
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = profile.email.ifBlank { "Add your email" },
                        fontSize = 16.sp,
                        color = GreyText
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color.White)
                    .border(1.dp, BorderGrey, RoundedCornerShape(26.dp))
            ) {
                SettingsRow(
                    title = "Large text",
                    subtitle = "Make words easier to read",
                    onClick = { onProfileChange(profile.copy(largeText = !profile.largeText)) },
                    leading = {
                        Text(
                            text = "Aa",
                            color = DarkGreen,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    trailing = {
                        Switch(
                            checked = profile.largeText,
                            onCheckedChange = { onProfileChange(profile.copy(largeText = it)) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = DarkGreen,
                                checkedBorderColor = DarkGreen,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = SwitchOffTrack,
                                uncheckedBorderColor = SwitchOffTrack
                            )
                        )
                    }
                )

                HorizontalDivider(color = BorderGrey)

                SettingsRow(
                    title = "My location",
                    subtitle = when {
                        detecting -> "Finding your location…"
                        profile.location.isBlank() -> "Tap to set your location"
                        else -> profile.location
                    },
                    onClick = { showLocationDialog = true },
                    leading = {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = null,
                            tint = DarkGreen
                        )
                    }
                )

                HorizontalDivider(color = BorderGrey)

                SettingsRow(
                    title = "Accessibility",
                    subtitle = "Display and interaction options",
                    onClick = {
                        try {
                            context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(
                                context,
                                "Accessibility settings aren't available on this device.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    leading = {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = null,
                            tint = DarkGreen
                        )
                    }
                )
            }

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(BeigeCard)
                    .padding(24.dp)
            ) {
                Text(
                    text = "Neighbourly Community Events",
                    color = DarkGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Bringing local people together, one event at a time.",
                    color = GreyText,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SignOutBackground)
                    .border(1.dp, SignOutBorder, RoundedCornerShape(18.dp))
                    .clickable { onSignOut() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign out",
                    color = Rust,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (showDetailsDialog) {
        DetailsDialog(
            name = profile.name,
            email = profile.email,
            onSave = { name, email ->
                onProfileChange(profile.copy(name = name, email = email))
                showDetailsDialog = false
            },
            onDismiss = { showDetailsDialog = false }
        )
    }

    if (showLocationDialog) {
        LocationDialog(
            location = profile.location,
            onSave = { location ->
                onProfileChange(profile.copy(location = location))
                showLocationDialog = false
            },
            onUseCurrent = {
                showLocationDialog = false
                useCurrentLocation()
            },
            onDismiss = { showLocationDialog = false }
        )
    }
}

@Composable
private fun SettingsRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    leading: @Composable () -> Unit,
    trailing: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(TileBackground),
            contentAlignment = Alignment.Center
        ) {
            leading()
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = DarkGreen,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = GreyText,
                fontSize = 15.sp
            )
        }

        trailing()
    }
}

@Composable
private fun DetailsDialog(
    name: String,
    email: String,
    onSave: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var nameInput by remember { mutableStateOf(name) }
    var emailInput by remember { mutableStateOf(email) }
    var submitted by remember { mutableStateOf(false) }

    val nameError = submitted && nameInput.isBlank()
    val emailError = submitted && !Patterns.EMAIL_ADDRESS.matcher(emailInput.trim()).matches()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = "Your details",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Name") },
                    singleLine = true,
                    isError = nameError,
                    supportingText = { if (nameError) Text("Please enter your name") },
                    shape = RoundedCornerShape(14.dp),
                    colors = profileFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    label = { Text("Email") },
                    singleLine = true,
                    isError = emailError,
                    supportingText = { if (emailError) Text("Please enter a valid email address") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(14.dp),
                    colors = profileFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    submitted = true
                    if (nameInput.isNotBlank() &&
                        Patterns.EMAIL_ADDRESS.matcher(emailInput.trim()).matches()
                    ) {
                        onSave(nameInput.trim(), emailInput.trim())
                    }
                }
            ) {
                Text("Save", color = Orange, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = GreyText)
            }
        }
    )
}

@Composable
private fun LocationDialog(
    location: String,
    onSave: (String) -> Unit,
    onUseCurrent: () -> Unit,
    onDismiss: () -> Unit
) {
    var input by remember { mutableStateOf(location) }
    var submitted by remember { mutableStateOf(false) }
    val error = submitted && input.isBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = "My location",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("Town or neighbourhood") },
                    singleLine = true,
                    isError = error,
                    supportingText = { if (error) Text("Please enter your location") },
                    shape = RoundedCornerShape(14.dp),
                    colors = profileFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                TextButton(onClick = onUseCurrent) {
                    Icon(
                        imageVector = Icons.Outlined.MyLocation,
                        contentDescription = null,
                        tint = Orange,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Use my current location", color = Orange, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    submitted = true
                    if (input.isNotBlank()) onSave(input.trim())
                }
            ) {
                Text("Save", color = Orange, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = GreyText)
            }
        }
    )
}

@Composable
private fun profileFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = DarkGreen,
    unfocusedTextColor = DarkGreen,
    focusedBorderColor = DarkGreen,
    unfocusedBorderColor = BorderGrey,
    focusedLabelColor = DarkGreen,
    cursorColor = Orange
)
