package com.djevannn.nftmarketplace.helper

import java.text.SimpleDateFormat
import java.util.*

class Utils {
}

interface ResponseCallback {
    fun getCallback(msg:String,status: Boolean)
}

fun getCurrentDate():String{
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    return simpleDateFormat.format(Date())
}