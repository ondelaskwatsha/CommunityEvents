package com.example.communityevents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.communityevents.navigation.CommunityEventsApp
import com.example.communityevents.ui.theme.CommunityEventsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CommunityEventsTheme {
                CommunityEventsApp()
            }
        }
    }
}