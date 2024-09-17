package com.matze.therprodkmp.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.MonthNames
import kotlin.math.roundToInt

fun Float.roundToDecimals(decimals: Int): Float {
    var dotAt = 1
    repeat(decimals) { dotAt *= 10 }
    val roundedValue = (this * dotAt).roundToInt()
    return (roundedValue / dotAt) + (roundedValue % dotAt).toFloat() / dotAt
}

//fun LocalDate.Format(): String {
//    monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); dayOfMonth(); chars(", "); year()
//}