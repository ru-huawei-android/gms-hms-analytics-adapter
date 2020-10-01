package com.huawei.adapter.analytics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.adapter.analytics.ComposedAnalytics
import kotlinx.android.synthetic.main.activity_main.*

class AnalyticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val composedAnalytics = ComposedAnalytics(this@AnalyticsActivity)

        val availableSDKs = composedAnalytics.getIntegrations()

        integrationsList.text = getString(R.string.available_analytics_sdks, availableSDKs)

        customEvent.setOnClickListener {
            val bundle = Bundle().apply {
                putString("customField1", "Hello")
                putString("customField2", "World")
            }
            composedAnalytics.onEvent("custom_event", bundle)
        }

        builtInEvent.setOnClickListener {
            composedAnalytics.onEvent("post_score", Bundle().apply { putLong("score", 100L) })
        }

        composedAnalytics.setUserProperty("test_property", "test_value")
    }

}