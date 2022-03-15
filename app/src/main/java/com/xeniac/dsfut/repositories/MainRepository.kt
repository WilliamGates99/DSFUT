package com.xeniac.dsfut.repositories

import com.xeniac.dsfut.api.RetrofitInstance

class MainRepository {

//    suspend fun pickUpPlayer(
//        fifaVersion: String, platform: String, partnerId: String, currentTime: String,
//        signature: String, minPrice: String?, maxPrice: String?, takeAfter: String?
//    ) = RetrofitInstance.api.pickUpPlayer(
//        fifaVersion, platform, partnerId, currentTime, signature, minPrice, maxPrice, takeAfter
//    )

    suspend fun pickUpPlayer(feedUrl: String) = RetrofitInstance.api.pickUpPlayer(feedUrl)

    suspend fun pickUpPlayerWithOptionals(
        feedUrl: String,
        minPrice: String? = null,
        maxPrice: String? = null,
        takeAfter: String? = null
    ) = RetrofitInstance.api.pickUpPlayerWithOptionals(feedUrl, minPrice, maxPrice, takeAfter)
}