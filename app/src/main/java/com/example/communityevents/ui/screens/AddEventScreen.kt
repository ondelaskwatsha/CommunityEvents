package com.example.communityevents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.communityevents.R
import com.example.communityevents.ui.theme.*

@Composable
fun AddEventScreen(onClose: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Top row: label + close X
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SHARE WITH NEIGHBOURS",
                color = Terracotta,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
            IconButton(onClick = onClose) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_exist_button),
                    contentDescription = "Close",
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Add an event",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Tell us the basics. We'll review it before it goes live.",
            fontSize = 14.sp,
            color = WarmGray
        )
        Spacer(Modifier.height(24.dp))

        // Fields
        FormField(label = "Event name", value = title, placeholder = "e.g. Sunday book club",
            onValueChange = { title = it })
        Spacer(Modifier.height(16.dp))

        FormField(label = "Date", value = date, placeholder = "yyyy/mm/dd",
            trailingIcon = R.drawable.ic_calender_icon,
            onValueChange = { date = it })
        Spacer(Modifier.height(16.dp))

        FormField(label = "Location", value = location, placeholder = "Venue or address",
            onValueChange = { location = it })
        Spacer(Modifier.height(16.dp))

        FormField(
            label = "Description",
            value = description,
            placeholder = "What can people expect?",
            singleLine = false,
            onValueChange = { description = it }
        )

        Spacer(Modifier.height(16.dp))

        // Error message
        errorMessage?.let {
            Text(text = it, color = RequiredRed, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
        }

        // Submit button
        Button(
            onClick = {
                errorMessage = validate(title, date, location, description)
                if (errorMessage == null) {
                    // TODO: save to Firestore + post to REST API (later)
                    onClose()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
        ) {
            Text("Submit event", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                color = Color.White)
            Spacer(Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_submit_arrow),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    trailingIcon: Int? = null,
    singleLine: Boolean = true
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontWeight = FontWeight.SemiBold, color = DarkText, fontSize = 14.sp)
            Text("REQUIRED", color = RequiredRed, fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
        }
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = WarmGray) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            minLines = if (singleLine) 1 else 4,
            shape = RoundedCornerShape(12.dp),
            trailingIcon = trailingIcon?.let {
                {
                    Icon(painterResource(id = it), contentDescription = null,
                        modifier = Modifier.size(20.dp))
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ForestGreen,
                unfocusedBorderColor = InputBorder,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}

private fun validate(title: String, date: String, location: String, description: String): String? {
    if (title.isBlank()) return "Event name is required."
    if (date.isBlank()) return "Date is required."
    if (location.isBlank()) return "Location is required."
    if (description.isBlank()) return "Description is required."
    return null
}
