package com.djevannn.nftmarketplace.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val about: String,
    val balance: Double,
    val created_at: String,
    val name: String,
    val password: String,
    val photo_url: String,
    val username: String,
    val wallet: String,
    val isLogin: Boolean,
) : Parcelable

@Parcelize
data class UserRegist(
    val about: String = "",
    val balance: Double = 0.0,
    val created_at: String = "",
    val name: String,
    val password: String,
    val photo_url: String = "",
    val username: String,
    val wallet: String = "",
) : Parcelable
