package com.xeniac.dsfut.repositories

import com.xeniac.dsfut.api.RetrofitInstance

class MainRepository {

    suspend fun pickUpPlayer(
        feedUrl: String,
        minPrice: String? = null,
        maxPrice: String? = null,
        takeAfter: String? = null
    ) = RetrofitInstance.api.pickUpPlayer(feedUrl, minPrice, maxPrice, takeAfter)
}