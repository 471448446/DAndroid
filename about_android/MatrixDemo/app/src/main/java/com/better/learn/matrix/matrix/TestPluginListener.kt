/*
 * Tencent is pleased to support the open source community by making wechat-matrix available.
 * Copyright (C) 2018 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.better.learn.matrix.matrix

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.better.learn.matrix.App
import com.tencent.matrix.plugin.DefaultPluginListener
import com.tencent.matrix.report.Issue
import com.tencent.matrix.util.MatrixLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.lang.ref.SoftReference
import kotlin.concurrent.thread

class TestPluginListener(context: Context) : DefaultPluginListener(context) {

    override fun onReportIssue(issue: Issue) {
        super.onReportIssue(issue)
        MatrixLog.e(TAG, issue.toString())
        Toast.makeText(App.shared, "检测到性能问题", Toast.LENGTH_SHORT).show()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("key", issue.key)
            jsonObject.put("type", issue.type)
            jsonObject.put("tag", issue.tag)
            jsonObject.put("type", issue.type)
            jsonObject.put("content", issue.content)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("Better", jsonObject.toString())
//        GlobalScope.launch(Dispatchers.IO) {
//            val file =
//                File(App.shared.getExternalFilesDir("matrix"), "${System.currentTimeMillis()}.txt")
//            file.writeText(jsonObject.toString())
//        }
    }

    companion object {
        const val TAG = "TestPluginListener"
    }

}