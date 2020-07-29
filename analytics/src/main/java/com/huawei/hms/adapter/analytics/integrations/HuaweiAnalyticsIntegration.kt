package com.huawei.hms.adapter.analytics.integrations

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.huawei.hms.adapter.analytics.TAG
import com.huawei.hms.adapter.analytics.utils.*
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability

class HuaweiAnalyticsIntegration : AnalyticsIntegration {

    override val integrationId: String
        get() = id

    private lateinit var hiAnalyticsInstance: HiAnalyticsInstance
    private var isStarted = false

    override fun init(context: Context) {
        if (isApiAvailable(context)) {
            HiAnalyticsTools.enableLog()
            hiAnalyticsInstance = HiAnalytics.getInstance(context)
            Log.i(TAG, "HiAnalytics was successfully started")
            isStarted = true
        } else {
            Log.i(TAG, "HiAnalytics wasn't started")
            isStarted = false
        }
    }

    override fun isStarted() = isStarted

    override fun logEvent(name: String, bundle: Bundle?) {
        val hmsEventName = eventsMap[name]
        if (hmsEventName != null) {
            val newBundle = Bundle()
            bundle?.keySet()?.forEach { key ->
                val hmsParamName = substitute(name, hmsEventName)
                if (hmsParamName != null) {
                    processParameter(bundle, newBundle, key, hmsParamName)
                } else {
                    Log.e(TAG, "can't map $key parameter to HMS analogue")
                }
            }
            addMandatoryParams(hmsEventName, newBundle)
            hiAnalyticsInstance.onEvent(hmsEventName, newBundle)
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

    override fun isApiAvailable(context: Context) = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context) == ConnectionResult.SUCCESS

    companion object {
        const val id = "HiAnalytics"
    }

}