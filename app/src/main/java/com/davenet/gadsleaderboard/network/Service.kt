package com.davenet.gadsleaderboard.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

/**
 * A retrofit service make network calls.
 */
private const val SUBMIT_URL = "https://docs.google.com/forms/d/e/1FAIpQLSf9d1TcNU6zc6KR8bSEM41Z1g1zl35cwZr2xyjIhaMAz8WChQ/formResponse"
interface LeaderboardService {
    @GET("hours")
    fun getLeaderHours(): Deferred<List<NetworkHour>>

    @GET("skilliq")
    fun getLeaderSkills(): Deferred<List<NetworkSkill>>

    @POST
    @FormUrlEncoded
    suspend fun submitProject(
        @Url url: String = SUBMIT_URL,
        @Field("entry.1877115667") firstName: String,
        @Field("entry.2006916086") lastName: String,
        @Field("entry.1824927963") emailAddress: String,
        @Field("entry.284483984") projectLink: String
    ): Response<Void>
}

/**
 * Main entry point for network access. Call like `LeaderNetwork.gads.getLeaderHours`
 */
object LeaderNetwork {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://gadsapi.herokuapp.com/api/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val gads = retrofit.create(LeaderboardService::class.java)
}