package com.huawei.hms.adapter.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.huawei.hms.adapter.analytics.integrations.AnalyticsIntegration
import com.huawei.hms.adapter.analytics.integrations.FirebaseAnalyticsIntegration
import com.huawei.hms.adapter.analytics.integrations.HuaweiAnalyticsIntegration

const val TAG = "AnalyticsAdapter"

object ComposedAnalytics : AnalyticsAdapter {
    private var wasServiceStarted = false

    private val integrations = arrayOf(HuaweiAnalyticsIntegration(), FirebaseAnalyticsIntegration())

    private val defaultIntegrationMap = mapOf(
        HuaweiAnalyticsIntegration.id to integrations[0],
        FirebaseAnalyticsIntegration.id to integrations[1]
    )

    private var availableIntegrations = mutableMapOf<String, AnalyticsIntegration>()

    override fun getSupportedAPIs() = defaultIntegrationMap.keys

    override fun getDeviceAPIs(): Set<String> {
        if (!wasServiceStarted) {
            Log.e(TAG, "AnalyticsAdapter was not properly initialized, call init() before")
            return emptySet()
        }

        return availableIntegrations.map { it.key }.toSet()
    }

    override fun init(context: Context, APIs: Set<String>) {
        if (wasServiceStarted) {
            Log.e(TAG, "Adapter was started previously, skipping action...")
            return
        }

        Log.i(TAG, "Adapter initialization has been started...")
        integrations.filter { APIs.contains(it.getId()) }.forEach {
            if (APIs.contains(it.getId())) {
                it.init(context)
                if (it.isStarted()) {
                    availableIntegrations[it.getId()] = it
                    Log.i(TAG, "${it.getId()} integration successfully launched")
                } else {
                    Log.e(TAG, "couldn't start ${it.getId()} integration")
                }
            }
        }
        wasServiceStarted = true
    }

    override fun onEvent(eventName: String, bundle: Bundle?, APIs: Set<String>) {
        if (!wasServiceStarted) {
            Log.e(TAG, "AnalyticsAdapter was not properly initialized, call init() before")
            return
        }

        APIs.forEach { integrationName ->
            defaultIntegrationMap[integrationName].let { integration ->
                if (integration == null || !integration.isStarted()) {
                    Log.e(TAG, "$integrationName event was requested, but not started, check service availability")
                } else {
                    integration.onEvent(eventName, bundle)
                }
            }
        }
    }

    override fun setUserProperty(name: String, value: String, APIs: Set<String>) {
        if (!wasServiceStarted) {
            Log.e(TAG, "AnalyticsAdapter was not properly initialized, call init() before")
            return
        }

        APIs.forEach { integrationName ->
            defaultIntegrationMap[integrationName].let { integration ->
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
    fun init(context: Context, APIs: Set<String> = getSupportedAPIs())

    fun getSupportedAPIs(): Set<String>
    fun getDeviceAPIs(): Set<String>

    fun onEvent(eventName: String, bundle: Bundle?, APIs: Set<String> = getDeviceAPIs())
    fun setUserProperty(name: String, value: String, APIs: Set<String> = getDeviceAPIs())
}
