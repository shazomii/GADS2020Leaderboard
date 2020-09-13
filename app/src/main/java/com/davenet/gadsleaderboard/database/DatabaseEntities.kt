package com.davenet.gadsleaderboard.database

import androidx.room.Entity
import com.davenet.gadsleaderboard.domain.LeaderHour
import com.davenet.gadsleaderboard.domain.LeaderSkill

/**
 * Database entities go in this file. These are responsible for reading and writing
 * from the database.
 */

/**
 * DatabaseHour represents a Learning Leader(hours spent learning) entity in the database.
 */
@Entity(primaryKeys = ["name", "hours", "country"])
data class DatabaseHour constructor(
    val name: String,
    val hours: Int,
    val country: String,
    val badgeUrl: String)

/**
 * DatabaseSkills represents a Learning Leader(skill IQ score) entity in the database
 */
@Entity(primaryKeys = ["name", "score", "country"])
data class DatabaseSkill constructor(
    val name: String,
    val score: Int,
    val country: String,
    val badgeUrl: String)

/**
 * Map DatabaseHours to domain entities
 */

@JvmName("asDomainModelDatabaseHour")
fun List<DatabaseHour>.asDomainModel(): List<LeaderHour> {
    return map {
        LeaderHour(
            name = it.name,
            hours = it.hours,
            country = it.country,
            badgeUrl = it.badgeUrl
        )
    }
}

@JvmName("asDomainModelDatabaseSkill")
fun List<DatabaseSkill>.asDomainModel(): List<LeaderSkill> {
    return map {
        LeaderSkill(
            name = it.name,
            score = it.score,
            country = it.country,
            badgeUrl = it.badgeUrl
        )
    }
}
