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

internal class HuaweiAnalyticsIntegration : AnalyticsIntegration {

    private lateinit var hiAnalyticsInstance: HiAnalyticsInstance
    override var isStarted = false

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

    override fun logEvent(name: String, bundle: Bundle?) {
        val hmsEventName = gmsToHmsEventMap[name]
        if (hmsEventName != null) {
            val newBundle = Bundle()
            bundle?.keySet()?.forEach { key ->
                val hmsParamName = transformGmsToHmsParameters(name, hmsEventName)
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

    override fun name() = id

    override fun isApiAvailable(context: Context): Boolean {
        val instance = HuaweiApiAvailability.getInstance()
        if (instance.isHuaweiMobileServicesAvailable(context) == ConnectionResult.SUCCESS) {
            val packageInfo = context.packageManager.getPackageInfo("com.huawei.hwid", 0)
            if (packageInfo.versionCode > 40004000) { //longVersionCode для API Level 28
                Log.e(TAG, "Old HMS Core detected, please update it to 5.x version")
                return false
            }
        }
        return instance.isHuaweiMobileServicesAvailable(context) == ConnectionResult.SUCCESS
    }

    companion object {
        const val id = "HiAnalytics"
    }

}