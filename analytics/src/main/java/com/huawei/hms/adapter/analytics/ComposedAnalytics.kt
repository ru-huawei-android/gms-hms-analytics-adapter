package com.huawei.hms.adapter.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.huawei.hms.adapter.analytics.integrations.FirebaseAnalyticsIntegration
import com.huawei.hms.adapter.analytics.integrations.HuaweiAnalyticsIntegration

class ComposedAnalytics constructor(context: Context) {

    private var wasServiceStarted = false

    private val integrationsList = arrayOf(HuaweiAnalyticsIntegration(), FirebaseAnalyticsIntegration())

    init {
        Log.i(TAG, "Adapter initialization has been started...")
        integrationsList.forEach { integration ->
            integration.init(context)
            if (integration.isStarted) {
                Log.i(TAG, "${integration.name()} integration successfully launched")
            } else {
                Log.e(TAG, "couldn't start ${integration.name()} integration")
            }
        }

        wasServiceStarted = true
    }

    fun getIntegrations(): List<String> {
        if (!wasServiceStarted) {
            Log.e(TAG, "AnalyticsAdapter was not properly initialized, call init() before")
            return emptyList()
        }

        return integrationsList.filter { it.isStarted }.map { it.name() }
    }

    fun onEvent(eventName: String, bundle: Bundle?) {
        if (!wasServiceStarted) {
            Log.e(TAG, "AnalyticsAdapter was not properly initialized, call init() before")
            return
        }

        integrationsList.forEach { integration ->
            if (integration.isStarted) {
                integration.logEvent(eventName, bundle)
            }
        }
    }

    fun setUserProperty(name: String, value: String) {
        if (!wasServiceStarted) {
            Log.e(TAG, "AnalyticsAdapter was not properly initialized, call init() before")
            return
        }

        integrationsList.forEach { integration ->
            if (integration.isStarted) {
                integration.onUserProfile(name, value)
            }
        }
    }

    companion object {
        private const val TAG = "AnalyticsAdapter"
    }
}