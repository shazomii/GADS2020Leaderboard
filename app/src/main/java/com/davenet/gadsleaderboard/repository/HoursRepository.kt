package com.davenet.gadsleaderboard.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.davenet.gadsleaderboard.database.LeaderboardDatabase
import com.davenet.gadsleaderboard.database.asDomainModel
import com.davenet.gadsleaderboard.domain.LeaderHour
import com.davenet.gadsleaderboard.network.LeaderNetwork
import com.davenet.gadsleaderboard.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HoursRepository(private val database: LeaderboardDatabase) {
    val hours: LiveData<List<LeaderHour>> = Transformations.map(database.hourDao.getHours()) {
        it.asDomainModel()
    }

    /**
     * Refresh the learners by hours stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using 'withContext', this
     * function is now safe to call from any thread including the Main thread.
     */
    suspend fun refreshHours() {
        withContext(Dispatchers.IO) {
            val leaderHours = LeaderNetwork.gads.getLeaderHours().await()
            database.hourDao.insertAll(leaderHours.asDatabaseModel())
        }
    }
}