package com.davenet.gadsleaderboard.network

import com.davenet.gadsleaderboard.database.DatabaseHour
import com.davenet.gadsleaderboard.database.DatabaseSkill
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

/**
 * Hours represent a learners learning hours
 */
@JsonClass(generateAdapter = true)
data class NetworkHour(
    val name: String,
    val hours: String,
    val country: String,
    val badgeUrl: String)

@JsonClass(generateAdapter = true)
data class NetworkSkill(
    val name: String,
    val score: String,
    val country: String,
    val badgeUrl: String
)

fun List<NetworkHour>.asDatabaseModel(): List<DatabaseHour> {
    return map {
        DatabaseHour(
            name = it.name,
            hours = it.hours,
            country = it.country,
            badgeUrl = it.badgeUrl)
    }
}

fun List<NetworkSkill>.asDataModel(): List<DatabaseSkill> {
    return map {
        DatabaseSkill(
            name = it.name,
            score = it.score,
            country = it.country,
            badgeUrl = it.badgeUrl)
    }
}