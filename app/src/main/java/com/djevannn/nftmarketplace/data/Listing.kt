package com.djevannn.nftmarketplace.data

data class Listing(
    val new_owner: String = "",
    val nft_token_id: Int = 0,
    val prev_owner: String = "",
    val purchase_price: Double = 0.0,
    val time: String = ""
)
