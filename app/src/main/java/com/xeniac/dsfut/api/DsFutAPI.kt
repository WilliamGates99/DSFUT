package com.xeniac.dsfut.api

import com.xeniac.dsfut.models.DsFutResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DsFutAPI {

    @GET("api")
    suspend fun pickUpPlayer(
        @Query("fifa")
        fifaVersion: Int,
        @Query("console")
        platform: String,
        @Query("partner_id")
        partnerId: Int,
        @Query("timestamp")
        currentTime: Long,
        @Query("signature")
        signature: String,
        @Query("min_buy")
        min_buy: String?,
        @Query("max_buy")
        max_buy: String?,
        @Query("take_after")
        take_after: String?
    ): Response<DsFutResponse>
}