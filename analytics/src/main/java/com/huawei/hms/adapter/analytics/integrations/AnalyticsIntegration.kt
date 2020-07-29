package com.huawei.hms.adapter.analytics.integrations

import android.content.Context
import android.os.Bundle

interface AnalyticsIntegration {

    val integrationId: String
    fun getId(): String = integrationId

    fun init(context: Context)
    fun isApiAvailable(context: Context): Boolean

    fun isStarted(): Boolean
    fun logEvent(name: String, bundle: Bundle?)
    fun onUserProfile(name: String, value: String)
}