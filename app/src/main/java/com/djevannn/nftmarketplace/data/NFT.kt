package com.djevannn.nftmarketplace.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NFT(
    val title: String? = "",
    val image_url: String = "",
    val current_price: Double = 0.0,
    val is_sale: Boolean = true
): Parcelable

