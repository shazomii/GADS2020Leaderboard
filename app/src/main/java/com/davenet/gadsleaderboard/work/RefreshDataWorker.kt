package com.davenet.gadsleaderboard.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.davenet.gadsleaderboard.database.getDatabase
import com.davenet.gadsleaderboard.repository.HoursRepository
import com.davenet.gadsleaderboard.repository.SkillsRepository
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.davenet.gadsleaderboard.work.RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val hoursRepository = HoursRepository(database)
        val skillsRepository = SkillsRepository(database)
        try {
            hoursRepository.refreshHours()
            skillsRepository.refreshSkills()
            Timber.d("Work request for sync is running")
        } catch (e: HttpException) {
            Timber.d("Could not refresh data")
            return Result.retry()
        }
        return Result.success()
    }

}