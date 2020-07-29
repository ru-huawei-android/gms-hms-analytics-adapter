package com.huawei.hms.adapter.analytics.integrations

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.huawei.hms.adapter.analytics.TAG

class FirebaseAnalyticsIntegration : AnalyticsIntegration {

    override val integrationId: String
        get() = id

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var isStarted = false

    override fun init(context: Context) {
        if (isApiAvailable(context)) {
            firebaseAnalytics = Firebase.analytics
            Log.d(TAG, "Firebase is successfully started")
            isStarted = true
        } else {
            Log.d(TAG, "Firebase wasn't started")
            isStarted = false
        }
    }

    override fun isStarted() = isStarted

    override fun logEvent(name: String, bundle: Bundle?) {
        firebaseAnalytics.logEvent(name, bundle)
    }

    override fun onUserProfile(name: String, value: String) {
        if (isStarted) {
            firebaseAnalytics.setUserProperty(name, value)
        } else if (!isStarted) {
            Log.e(TAG, "setUserProperty() invoked, but not started, check HMS service availability")
        }
    }

    override fun isApiAvailable(context: Context) = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == com.google.android.gms.common.ConnectionResult.SUCCESS

    companion object {
        const val id = "FirebaseAnalytics"
    }
}