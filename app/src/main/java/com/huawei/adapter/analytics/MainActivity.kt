package com.huawei.adapter.analytics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.adapter.analytics.ComposedAnalytics
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        ComposedAnalytics.init(this@MainActivity)

        val availableSDKs = ComposedAnalytics.getSupportedAPIs().toString()
        val availableAPIs = ComposedAnalytics.getDeviceAPIs().toString()

        availableSDK.text = getString(R.string.available_analytics_sdks, availableSDKs)
        availableAPI.text = getString(R.string.available_on_device_apis, availableAPIs)

        customEvent.setOnClickListener {
            val bundle = Bundle().apply {
                putString("customField1", "Hello")
                putString("customField2", "World")
            }
            ComposedAnalytics.onEvent("custom_event", bundle)
        }

        builtInEvent.setOnClickListener {
            ComposedAnalytics.onEvent("post_score", Bundle().apply { putLong("score", 100L) })
        }
    }

}