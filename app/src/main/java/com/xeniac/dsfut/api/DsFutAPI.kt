package com.xeniac.dsfut.api

import com.xeniac.dsfut.models.DsFutResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DsFutAPI {

    @GET("api")
    suspend fun pickUpPlayer(
        @Query("fifa")
        fifaVersion: String,
        @Query("console")
        platform: String,
        @Query("partner_id")
        partnerId: String,
        @Query("timestamp")
        currentTime: String,
        @Query("signature")
        signature: String,
        @Query("min_buy")
        minPrice: String? = null,
        @Query("max_buy")
        maxPrice: String? = null,
        @Query("take_after")
        takeAfter: String? = null
    ): Response<DsFutResponse>
}