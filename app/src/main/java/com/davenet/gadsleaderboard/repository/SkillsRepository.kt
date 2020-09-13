package com.davenet.gadsleaderboard.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.davenet.gadsleaderboard.database.LeaderboardDatabase
import com.davenet.gadsleaderboard.database.asDomainModel
import com.davenet.gadsleaderboard.domain.LeaderSkill
import com.davenet.gadsleaderboard.network.LeaderNetwork
import com.davenet.gadsleaderboard.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SkillsRepository(private val database: LeaderboardDatabase) {
    val skills: LiveData<List<LeaderSkill>> = Transformations.map(database.skillDao.getSkills()) {
        it.asDomainModel()
    }

    /**
     * Refresh the learners by hours stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using 'withContext', this
     * function is now safe to call from any thread including the Main thread.
     */
    suspend fun refreshSkills() {
        withContext(Dispatchers.IO) {
            val leaderSkills = LeaderNetwork.gads.getLeaderSkills().await()
            database.skillDao.insertAll(leaderSkills.asDatabaseModel())
        }
    }
}