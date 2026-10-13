package com.example.communityevents.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.communityevents.R
import com.example.communityevents.ui.theme.Cream
import com.example.communityevents.ui.theme.ForestGreen
import com.example.communityevents.ui.theme.Terracotta

data class BottomNavItem(
    val route: String,
    val label: String,
    val iconRes: Int
)

val bottomNavItems = listOf(
    BottomNavItem("explore", "Explore", R.drawable.ic_home_icon_house),
    BottomNavItem("saved",   "Saved",   R.drawable.ic_heart_icon),
    BottomNavItem("add",     "Add event", R.drawable.ic_plus_circle),
    BottomNavItem("profile", "Profile", R.drawable.ic_person)
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    NavigationBar(containerColor = Cream) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp),
                        tint = if (selected) Terracotta else ForestGreen
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        color = if (selected) Terracotta else ForestGreen
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}