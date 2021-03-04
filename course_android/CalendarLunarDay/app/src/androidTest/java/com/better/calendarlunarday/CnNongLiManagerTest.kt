package com.better.calendarlunarday

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.better.calendarlunarday.nongliManager.CnNongLiManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CnNongLiManagerTest {
    companion object {
        const val TAG = "Lunar"
    }

    lateinit var cnNongLiManager: CnNongLiManager

    @Before
    fun before() {
        cnNongLiManager = CnNongLiManager()
    }

    @Test
    fun calGongliToNongli() {
        val nongli = cnNongLiManager.calGongliToNongli(2021, 3, 4)
        Log.d(TAG, nongli.joinToString(","))
    }

}