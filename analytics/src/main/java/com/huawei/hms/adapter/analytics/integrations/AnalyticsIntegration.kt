package com.huawei.hms.adapter.analytics.integrations

import android.content.Context
import android.os.Bundle

internal interface AnalyticsIntegration {
    var isStarted: Boolean

    fun init(context: Context)

    fun isApiAvailable(context: Context): Boolean
    fun logEvent(name: String, bundle: Bundle?)
    fun onUserProfile(name: String, value: String)

    fun name() : String
}