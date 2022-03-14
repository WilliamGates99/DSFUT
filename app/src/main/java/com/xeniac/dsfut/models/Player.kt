package com.xeniac.dsfut.models

data class Player(
    val assetID: Int,
    val buyNowPrice: Int,
    val expires: Int,
    val name: String,
    val position: String,
    val rating: Int,
    val resourceID: Int,
    val startPrice: Int,
    val tradeID: Long,
    val transactionID: Int
)