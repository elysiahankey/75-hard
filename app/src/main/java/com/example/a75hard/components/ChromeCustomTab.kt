package com.example.a75hard.components

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

fun openChromeCustomTab(url: String, context: Context) {
    val intent = CustomTabsIntent.Builder()
        .build()
    intent.launchUrl(context, url.toUri())
}