package com.davenet.gadsleaderboard.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * HourDao Interface with helper methods
 * getHours() method fetches all the learning leaders based on hours from the database
 * insertAll() method inserts learning leaders into the database
 */
@Dao
interface HourDao {
    @Query("select * from databasehour")
    fun getHours(): LiveData<List<DatabaseHour>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(hours: List<DatabaseHour>)
}

/**
 * SkillDao Interface with helper methods
 * getSkills() method fetches all the learning leaders based on skill IQ score from the database
 * insertAll() method inserts learning leaders into the database
 */
@Dao
interface SkillDao {
    @Query("select * from databaseskill")
    fun getSkills(): LiveData<List<DatabaseSkill>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(skills: List<DatabaseSkill>)
}

/**
 * Database for the offline cache
 */
@Database(entities = [DatabaseHour::class, DatabaseSkill::class], version = 1)
abstract class LeaderboardDatabase: RoomDatabase() {
    abstract val hourDao: HourDao
    abstract val skillDao: SkillDao
}

/**
 * Singleton INSTANCE to prevent having multiple instances of
 * the same database opened at the same time.
 */
private lateinit var INSTANCE: LeaderboardDatabase

fun getDatabase(context: Context): LeaderboardDatabase {
    synchronized(LeaderboardDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
            LeaderboardDatabase::class.java,
            "leaderboard").build()
        }
    }
    return INSTANCE
}