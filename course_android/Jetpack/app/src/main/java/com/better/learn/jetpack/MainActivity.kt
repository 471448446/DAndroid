package com.better.learn.jetpack

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.GuardedBy
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.better.learn.jetpack.app_startup.InitializeActivity
import com.better.learn.jetpack.databinding.ActivityMainBinding
import com.better.learn.jetpack.datastore.dataStorePreference
import com.better.learn.jetpack.datastore.dataStoreProtobuf
import com.better.learn.jetpack.start_activity_result.StartedActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        jStartActivityForResult()
        jAppStartup()
        jDataStore()
        testThread()
    }

    private fun testThread() {
        val test = TestThreadSafety()
        test.money = 100
    }

    @SuppressLint("SetTextI18n")
    private fun jDataStore() {
        // 1. 申明KEY
        val key = intPreferencesKey("click_btn_times")
        lifecycleScope.launch {
            // 2. 根据KEY取值
            val times = dataStorePreference.data.map {
                // 取值，以及默认值。[]取出来的类型不一定安全
                it[key] ?: 0
            }.first()
//            dataStorePreference.data.collect {
//                Log.i("Better",it.toString())
//            }
            val times2 = dataStoreProtobuf.data.map {
                it.clickBtnTimes
            }.first()
            if (0 != times) {
                binding.dataStore.text = "DataStore click \n preference:$times,protobuf:$times2"
            }
        }
        binding.dataStore.setOnClickListener {
            lifecycleScope.launch {
                val t1 = dataStorePreference.data.map {
                    // 取值，以及默认值。[]取出来的类型不一定安全
                    it[key] ?: 0
                }.first() + 1
                val t2 = dataStoreProtobuf.data.map {
                    it.clickBtnTimes
                }.first() + 1
                // 3. 保存KEY的值
                dataStorePreference.edit {
                    it[key] = t1
                    Log.d("Better", "update()： ${Thread.currentThread().name}")
                }
                dataStoreProtobuf.updateData { current ->
                    current.toBuilder()
                        .setClickBtnTimes(t2)
                        .build()
                }
                binding.dataStore.text = "DataStore click \n preference:$t1,protobuf:$t2"
            }
        }
    }

    private fun jAppStartup() {
        binding.appStartup.setOnClickListener {
            startActivity(Intent(this, InitializeActivity::class.java))
        }
    }

    private fun jStartActivityForResult() {
        // 1. 获取所有结果 ActivityResultContracts.StartActivityForResult()
        // 2. 只获取url：ActivityResultContracts.GetContent()
        val startActivity =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    val back = result.data?.getStringExtra("haha") ?: "fail get back string"
                    Toast.makeText(this, back, Toast.LENGTH_SHORT).show()
                }
            }

        binding.startActivityForResult.setOnClickListener {
            startActivity.launch(Intent(this, StartedActivity::class.java))
        }
    }
}

class TestThreadSafety {
    private val moneyLock = Any()

    @GuardedBy("moneyLock")
    var money: Int = 0
}