package com.xeniac.dsfut.repositories

import com.xeniac.dsfut.api.RetrofitInstance

class MainRepository {

    suspend fun pickUpPlayer(
        fifaVersion: Int, platform: String, partnerId: Int, currentTime: Long,
        signature: String, minPrice: Int?, maxPrice: Int?, takeAfter: Int?
    ) = RetrofitInstance.api.pickUpPlayer(
        fifaVersion, platform, partnerId, currentTime, signature, minPrice, maxPrice, takeAfter
    )
}