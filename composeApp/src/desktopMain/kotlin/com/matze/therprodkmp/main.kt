package com.matze.therprodkmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.matze.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "TherProdKMP",
    ) {
        App()
    }
}