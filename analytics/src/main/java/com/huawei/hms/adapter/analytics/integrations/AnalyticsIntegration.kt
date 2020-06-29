package com.huawei.hms.adapter.analytics.integrations

import android.content.Context
import android.os.Bundle

interface AnalyticsIntegration {

    val integrationId: String

    fun init(context: Context)
    fun isApiAvailable(context: Context): Boolean

    fun getId(): String = integrationId
    fun isStarted(): Boolean
    fun onEvent(name: String, bundle: Bundle?)
    fun onUserProfile(name: String, value: String)
}