package com.djevannn.nftmarketplace.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NFT(
    val title: String? = "",
    val description: String = "",
    val owners: List<String> = arrayListOf(),
    val image_url: String = "",
    val current_price: Double = 0.0,
    val creator: String = "",
    val creator_fee: Double = 0.0,
    val is_sale: Boolean = true
): Parcelable

