package com.huawei.hms.adapter.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.huawei.hms.adapter.analytics.integrations.FirebaseAnalyticsIntegration
import com.huawei.hms.adapter.analytics.integrations.HiAnalyticsIntegration

const val TAG = "AnalyticsAdapter"

object ComposedAnalytics : AnalyticsAdapter {
    private var wasServiceStarted = false

    private val integrations = arrayOf(HiAnalyticsIntegration(), FirebaseAnalyticsIntegration())
    private val integrationMap = mapOf(
        HiAnalyticsIntegration.id to integrations[0],
        FirebaseAnalyticsIntegration.id to integrations[1]
    )

    override fun getSupportedIntegrations() = integrationMap.keys

    override fun init(context: Context, integrationIds: Set<String>) {
        Log.d(TAG, "Adapter initialization has been started...")
        integrations.filter { integrationIds.contains(it.getId()) }.forEach {
            if (integrationIds.contains(it.getId())) {
                it.init(context)
            }
        }
        wasServiceStarted = true
    }

    override fun onEvent(eventName: String, bundle: Bundle?, integrationIds: Set<String>) {
        if (!wasServiceStarted) {
            Log.e(TAG, "AnalyticsAdapter was not properly initialized, call init() before")
            return
        }

        integrationIds.forEach { integrationName ->
            integrationMap[integrationName].let { integration ->
                if (integration == null || !integration.isStarted()) {
                    Log.e(TAG, "$integrationName event was requested, but not started, check service availability")
                } else {
                    integration.onEvent(eventName, bundle)
                }
            }
        }
    }

    override fun setUserProperty(name: String, value: String, integrationIds: Set<String>) {
        if (!wasServiceStarted) {
            Log.e(TAG, "AnalyticsAdapter was not properly initialized, call init() before")
            return
        }

        integrationIds.forEach { integrationName ->
            integrationMap[integrationName].let { integration ->
                if (integration == null || !integration.isStarted()) {
                    Log.e(TAG, "$integrationName userProfile was requested, but not started, check service availability")
                } else {
                    integration.onUserProfile(name, value)
                }
            }
        }
    }
}

interface AnalyticsAdapter {
    fun getSupportedIntegrations(): Set<String>

    fun init(context: Context, integrationIds: Set<String> = getSupportedIntegrations())
    fun onEvent(eventName: String, bundle: Bundle?, integrationIds: Set<String> = getSupportedIntegrations())
    fun setUserProperty(name: String, value: String, integrationIds: Set<String> = getSupportedIntegrations())
}
