package com.huawei.hms.adapter.analytics.integrations

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.huawei.hms.adapter.analytics.ComposedAnalytics
import com.huawei.hms.adapter.analytics.GMS2HMSEvents
import com.huawei.hms.adapter.analytics.TAG
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability

class FirebaseAnalyticsIntegration : AnalyticsIntegration {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var isStarted = false

    override fun init(context: Context) {
        if (isAvailable(context) == AvailabilityState.Available) {
            firebaseAnalytics = Firebase.analytics
            Log.d(TAG, "Firebase is successfully started")
            isStarted = true
        } else {
            Log.d(TAG, "Firebase wasn't started")
            isStarted = false
        }
    }

    override fun getId() = id

    override fun isStarted() = isStarted

    override fun onEvent(name: String, bundle: Bundle?) {
        firebaseAnalytics.logEvent(name, bundle)
    }

    override fun onUserProfile(name: String, value: String) {
        if (isStarted) {
            firebaseAnalytics.setUserProperty(name, value)
        } else if (!isStarted) {
            Log.e(TAG, "setUserProperty() invoked, but not started, check HMS service availability")
        }
    }

    private fun isAvailable(context: Context) =
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == com.google.android.gms.common.ConnectionResult.SUCCESS) {
            AvailabilityState.Available
        } else {
            AvailabilityState.Unavailable
        }

    companion object {
        val id = "FirebaseAnalytics"
    }
}