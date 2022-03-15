package com.xeniac.dsfut.repositories

import com.xeniac.dsfut.api.RetrofitInstance

class MainRepository {

    suspend fun pickUpPlayer(
        feedUrl: String, minPrice: Int?, maxPrice: Int?, takeAfter: Int?
    ) = RetrofitInstance.api.pickUpPlayer(feedUrl, minPrice, maxPrice, takeAfter)
}