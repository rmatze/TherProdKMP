package com.matze.therprodkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform