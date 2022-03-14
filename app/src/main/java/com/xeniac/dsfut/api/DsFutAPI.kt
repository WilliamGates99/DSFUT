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
        minPrice: Int? = null,
        @Query("max_buy")
        maxPrice: Int? = null,
        @Query("take_after")
        takeAfter: Int? = null
    ): Response<DsFutResponse>
}