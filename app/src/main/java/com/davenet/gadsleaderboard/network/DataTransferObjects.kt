package com.davenet.gadsleaderboard.network

import com.davenet.gadsleaderboard.database.DatabaseHour
import com.davenet.gadsleaderboard.database.DatabaseSkill
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to database objects before
 * using them.
 */

/**
 * NetworkHour represents a learner's learning hours
 */
@JsonClass(generateAdapter = true)
data class NetworkHour(
    val name: String,
    val hours: Int,
    val country: String,
    val badgeUrl: String
) {
    val id: Int = 0
}

/**
 * Map network object to database entities
 */
@JvmName("asDatabaseModelNetworkHour")
fun List<NetworkHour>.asDatabaseModel(): List<DatabaseHour> {
    return mapIndexed { index, it ->
        DatabaseHour(
            name = it.name,
            hours = it.hours,
            country = it.country,
            badgeUrl = it.badgeUrl,
            id = index
        )
    }
}

/**
 * NetworkSkill represents a learner's skillIQ score details
 */
@JsonClass(generateAdapter = true)
data class NetworkSkill(
    val name: String,
    val score: Int,
    val country: String,
    val badgeUrl: String
) {
    val id: Int = 0
}

/**
 * Map network object to database entities
 */
@JvmName("asDatabaseModelNetworkSkill")
fun List<NetworkSkill>.asDatabaseModel(): List<DatabaseSkill> {
    return mapIndexed {index, it ->
        DatabaseSkill(
            name = it.name,
            score = it.score,
            country = it.country,
            badgeUrl = it.badgeUrl,
            id = index
        )
    }
}