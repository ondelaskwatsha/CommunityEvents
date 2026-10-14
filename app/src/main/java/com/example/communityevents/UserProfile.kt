package com.example.communityevents



import android.content.Context

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val location: String = "",
    val largeText: Boolean = false
)

class ProfileStore(context: Context) {
    private val prefs = context.applicationContext
        .getSharedPreferences("profile", Context.MODE_PRIVATE)

    fun load() = UserProfile(
        name = prefs.getString("name", "") ?: "",
        email = prefs.getString("email", "") ?: "",
        location = prefs.getString("location", "") ?: "",
        largeText = prefs.getBoolean("largeText", false)
    )

    fun save(profile: UserProfile) {
        prefs.edit()
            .putString("name", profile.name)
            .putString("email", profile.email)
            .putString("location", profile.location)
            .putBoolean("largeText", profile.largeText)
            .apply()
    }
}

fun initialsOf(name: String): String =
    name.trim()
        .split(Regex("\\s+"))
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
