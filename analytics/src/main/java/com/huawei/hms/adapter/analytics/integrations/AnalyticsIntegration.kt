package com.huawei.hms.adapter.analytics.integrations

import android.content.Context
import android.os.Bundle

interface AnalyticsIntegration {
    fun init(context: Context)
    fun getId(): String
    fun isStarted(): Boolean
    fun onEvent(name: String, bundle: Bundle?)
    fun onUserProfile(name: String, value: String)
}

enum class AvailabilityState {
    Available,
    Unavailable
}