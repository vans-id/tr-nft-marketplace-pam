package com.djevannn.nftmarketplace.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NFT(
    var title: String = "",
    var owner: String = "",
    var image_url: String = "",
    var current_price: Double = 0.0,
    var creator: String = "",
    var token_id: Int = 0,
): Parcelable

