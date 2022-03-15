package com.xeniac.dsfut.api

import com.xeniac.dsfut.models.DsFutResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface DsFutAPI {

    @GET
    suspend fun pickUpPlayer(
        @Url feedUrl: String,
        @Query("min_buy")
        minPrice: Int?,
        @Query("max_buy")
        maxPrice: Int?,
        @Query("take_after")
        takeAfter: Int?
    ): Response<DsFutResponse>
}