package com.djevannn.nftmarketplace.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Creator(
    val about: String = "",
    val image_url: String = "",
    val name: String = "",
    val wallet: String = "",
) : Parcelable
