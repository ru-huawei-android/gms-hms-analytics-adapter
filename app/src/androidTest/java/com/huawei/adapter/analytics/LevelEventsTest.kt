package com.huawei.adapter.analytics

import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huawei.hms.adapter.analytics.utils.mapEvent
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LevelEventsTest {
    @Test
    fun startLevelTest() {
        val eventBundle = Bundle().apply {
            putLong("level_name", 1L)
        }

        val result = mapEvent("level_start", eventBundle)

        Assert.assertEquals(result.first, "\$StartLevel")
        Assert.assertTrue(result.second!!.containsKey("\$LevelName"))
        Assert.assertEquals(result.second!!.size(), 1)
    }

    @Test
    fun endLevelTest() {
        val eventBundle = Bundle().apply {
            putLong("level_name", 1L)
            putString("success", "true")
        }

        val result = mapEvent("level_end", eventBundle)

        Assert.assertEquals(result.first, "\$CompleteLevel")
        Assert.assertTrue(result.second!!.containsKey("\$LevelName"))
        Assert.assertTrue(result.second!!.containsKey("\$Result"))
        Assert.assertEquals(result.second!!.size(), 2)
    }

    @Test
    fun levelUpTest() {
        val eventBundle = Bundle().apply {
            putLong("level", 1L)
            putString("character", "char1")
        }

        val result = mapEvent("level_up", eventBundle)

        Assert.assertEquals(result.first, "\$UpgradeLevel")
        Assert.assertTrue(result.second!!.containsKey("\$LevelId"))
        Assert.assertTrue(result.second!!.containsKey("\$RoleName"))
        Assert.assertEquals(result.second!!.size(), 2)
    }
}