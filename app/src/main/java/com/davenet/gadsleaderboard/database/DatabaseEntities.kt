package com.davenet.gadsleaderboard.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.davenet.gadsleaderboard.domain.LeaderHour
import com.davenet.gadsleaderboard.domain.LeaderSkill

/**
 * Database entities go in this file. These are responsible for reading and writing
 * from the database.
 */

/**
 * DatabaseHour represents a Learning Leader(hours spent learning) entity in the database.
 */
@Entity
data class DatabaseHour constructor(
    val name: String,
    @PrimaryKey
    val hours: String,
    val country: String,
    val badgeUrl: String)

/**
 * DatabaseSkills represents a Learning Leader(skill IQ score) entity in the database
 */
@Entity
data class DatabaseSkill constructor(
    val name: String,
    @PrimaryKey
    val score: String,
    val country: String,
    val badgeUrl: String)

/**
 * Map DatabaseHours to domain entities
 */

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


fun List<DatabaseSkill>.asDomModel(): List<LeaderSkill> {
    return map {
        LeaderSkill(
            name = it.name,
            score = it.score,
            country = it.country,
            badgeUrl = it.badgeUrl
        )
    }
}
