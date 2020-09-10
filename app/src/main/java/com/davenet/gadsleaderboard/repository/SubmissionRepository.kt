package com.davenet.gadsleaderboard.repository

import com.davenet.gadsleaderboard.domain.Response
import com.davenet.gadsleaderboard.network.LeaderNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SubmissionRepository {
    suspend fun submitProject(firstName: String,
                              lastName: String,
                              emailAddress: String,
                              projectLink: String): Response<Int> {
       return try {
            withContext(Dispatchers.IO) {
                val result = LeaderNetwork.gads.submitProject(firstName = firstName,
                    lastName = lastName,
                    emailAddress = emailAddress,
                    projectLink = projectLink).code()
                if (result == 200) {
                    Response.Success(result)
                } else {
                    Response.Error("Submission not successful", null)
                }
            }
        } catch (error: Throwable) {
            Response.Error(error.message.toString(), null)
        }
    }
}