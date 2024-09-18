package com.matze

import androidx.compose.runtime.Composable
import com.matze.therprodkmp.TherProdApp
import com.matze.therprodkmp.ui.theme.TherProdTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    TherProdTheme {
        TherProdApp()
    }
}