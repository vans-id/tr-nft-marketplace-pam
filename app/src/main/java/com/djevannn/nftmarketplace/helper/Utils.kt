package com.djevannn.nftmarketplace.helper

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


interface ResponseCallback {
    fun getCallback(msg: String, status: Boolean)
}

fun getCurrentDate(): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    return simpleDateFormat.format(Date())
}

fun formatNumber(myNumber: Double): String {
    return if (myNumber <= 0) "0.00" else {
        val formatter: NumberFormat = DecimalFormat("#,###.00")
        formatter.format(myNumber)
    }
}

fun generateETHWallet(): String = "0x" + List(40) {
    (('0'..'9') + ('A'..'F') + ('a'..'f')).random()
}.joinToString("")