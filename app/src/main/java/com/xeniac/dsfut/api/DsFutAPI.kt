package com.xeniac.dsfut.api

import com.xeniac.dsfut.models.DsFutResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface DsFutAPI {

    @GET
    suspend fun pickUpPlayer(@Url feedUrl: String): Response<DsFutResponse>

    @GET
    suspend fun pickUpPlayerWithOptionals(
        @Url feedUrl: String,
        @Query("min_buy")
        minPrice: String? = null,
        @Query("max_buy")
        maxPrice: String? = null,
        @Query("take_after")
        takeAfter: String? = null
    ): Response<DsFutResponse>
}