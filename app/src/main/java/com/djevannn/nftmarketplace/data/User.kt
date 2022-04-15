package com.djevannn.nftmarketplace.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val username: String,
    val password: String,
    val wallet:String ,
    val created_at:String,
    val photo_url:String ,
    val isLogin: Boolean,
): Parcelable

@Parcelize
data class UserRegist(
    val name: String,
    val username: String,
    val password: String,
    val wallet:String ,
    val created_at:String,
    val photo_url:String ,
): Parcelable
