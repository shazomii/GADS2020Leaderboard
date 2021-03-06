package com.davenet.gadsleaderboard.domain

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *
 * @see database for objects that are mapped to the database
 * @see network for objects that parse or prepare network calls
 */

data class LeaderHour(
    val name: String,
    val hours: Int,
    val country: String,
    val badgeUrl: String
)

data class LeaderSkill(
    val name: String,
    val score: Int,
    val country: String,
    val badgeUrl: String
)