package com.better.learn.coroutineexception

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * https://blog.mindorks.com/exception-handling-in-kotlin-coroutines
 * 演示三类错误
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val model = ViewModelProvider(this).get(MainModel::class.java)
        model.hello()
        no_handle_error.setOnClickListener {
            model.errorNoHandle()
        }
        error_generic_hand.setOnClickListener {
            model.errorGenericHand()
        }
        error_error_handler.setOnClickListener {
            model.errorErrorHandler()
        }
        error_supervisor_parral.setOnClickListener {
            model.errorErrorSupervisor()
        }
        error_supervisor_parral_scope.setOnClickListener {
            model.errorErrorScope()
        }
        error_supervisor_parallel_supervisorscope.setOnClickListener {
            model.errorErrorSupervisorScope()
        }
    }
}

class MainModel : ViewModel() {
    val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("Better", "handle exception: $throwable")
    }

    fun hello() {
        Log.e("Better", "hello");
    }

    fun errorNoHandle() {
        viewModelScope.launch {
            val api = Api()
            api.simpleGet()
            api.errorGet()
        }
    }

    fun errorGenericHand() {
        viewModelScope.launch {
            val api = Api()
            try {
                api.simpleGet()
                api.errorGet()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun errorErrorHandler() {
        viewModelScope.launch(errorHandler) {
            val api = Api()
            api.simpleGet()
            api.errorGet()
        }
    }

    fun errorErrorSupervisor() {
        viewModelScope.launch {
//           直接 catch 不了的
            try {
                val api = Api()
                val oneDef = async {
                    api.simpleGet()
                }
                val twoDef = async {
                    api.errorGet()
                }
                val result1 = oneDef.await()
                val result2 = twoDef.await()
                Log.e("Better", "$result1,$result2")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun errorErrorScope() {
        viewModelScope.launch {
            try {
                // 发生错误会取消其他的task
                coroutineScope {
                    val api = Api()
                    val oneDef = async {
                        api.simpleGet()
                    }
                    val twoDef = async {
                        api.errorGet()
                    }
                    val result2 = twoDef.await()
                    val result1 = oneDef.await()
                    // 这里也是可以catch 异常，让流程继续执行下去。但是就没有利用到，自动取消其他任务的特点
//                    val result2: List<String> = try {
//                        twoDef.await()
//                    } catch (e: Exception) {
//                        emptyList()
//                    }
                    Log.e("Better", "$result1,$result2")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun errorErrorSupervisorScope() {
        viewModelScope.launch {
            try {
                // 发生错误不会取消其他的task
                supervisorScope {
                    val api = Api()
                    val oneDef = async {
                        api.simpleGet()
                    }
                    val twoDef = async {
                        api.errorGet()
                    }
                    val result1 = oneDef.await()
                    val result2: List<String> = try {
                        twoDef.await()
                    } catch (e: Exception) {
                        emptyList()
                    }

                    Log.e("Better", "$result1,$result2")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
