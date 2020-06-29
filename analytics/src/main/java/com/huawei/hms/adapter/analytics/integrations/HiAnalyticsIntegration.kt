package com.huawei.hms.adapter.analytics.integrations

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.huawei.hms.adapter.analytics.GMS2HMSEvents
import com.huawei.hms.adapter.analytics.GMS2HMSParams
import com.huawei.hms.adapter.analytics.TAG
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability

class HiAnalyticsIntegration : AnalyticsIntegration {

    private lateinit var hiAnalyticsInstance: HiAnalyticsInstance
    private var isStarted = false

    override fun init(context: Context) {
        if (isAvailable(context) == AvailabilityState.Available) {
            HiAnalyticsTools.enableLog()
            hiAnalyticsInstance = HiAnalytics.getInstance(context)
            Log.i(TAG, "HiAnalytics was successfully started")
            isStarted = true
        } else {
            Log.i(TAG, "HiAnalytics wasn't started")
            isStarted = false
        }
    }

    override fun getId() = id

    override fun isStarted() = isStarted

    override fun onEvent(name: String, bundle: Bundle?) {
        val mappedHMSEvent = GMS2HMSEvents[name]
        if (mappedHMSEvent != null) {
            val newBundle = Bundle()
            bundle?.keySet()?.forEach { key ->
                val hmsParamName = GMS2HMSParams[key]
                if (hmsParamName != null) {
                    processParameter(bundle, newBundle, key, hmsParamName)
                } else {
                    Log.e(TAG, "can't map $key parameter to HMS analogue")
                }
            }
            hiAnalyticsInstance.onEvent(mappedHMSEvent, newBundle)
        } else {
            hiAnalyticsInstance.onEvent(name, bundle)
        }
    }

    override fun onUserProfile(name: String, value: String) {
        if (isStarted) {
            hiAnalyticsInstance.setUserProfile(name, value)
        } else if (!isStarted) {
            Log.e(TAG, "setUserProfile() invoked, but not started, check HMS service availability")
        }
    }

    private fun isAvailable(context: Context) = if (HuaweiApiAvailability.getInstance()
            .isHuaweiMobileServicesAvailable(context) == ConnectionResult.SUCCESS
    ) {
        AvailabilityState.Available
    } else {
        AvailabilityState.Unavailable
    }

    fun processParameter(bundle: Bundle, newBundle: Bundle, key: String?, newKey: String?) {
        when (val value = bundle.get(key)) {
            is Int -> newBundle.putInt(newKey, value)
            is Long -> newBundle.putLong(newKey, value)
            is String -> newBundle.putString(newKey, value)
            is Byte -> newBundle.putByte(newKey, value)
            is Char -> newBundle.putChar(newKey, value)
            is Boolean -> newBundle.putBoolean(newKey, value)
        }
    }

    companion object {
        val id = "HiAnalytics"
    }

}