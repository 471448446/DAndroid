package com.better.learn.hellowebsocket

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okio.ByteString

/**
 * 使用OkHttp（从3.6.0起支持） 模拟webSocket调用
 * des:
 * https://zh.wikipedia.org/wiki/WebSocket
 * https://www.ruanyifeng.com/blog/2017/05/websocket.html
 * demo:
 * https://medium.com/@ssaurel/learn-to-use-websockets-on-android-with-okhttp-ba5f00aea988
 * URL:[http://www.websocket.org/echo.html]
 * OkHttp 中是 non-blocking 不阻塞 而 JavaWebSocket 可以选择
 * @author Better
 * @date 2020/7/31 10:48
 */
class OkHttpActivity : AppCompatActivity() {
    private val socketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            log("onOpen()", response.code, response.message)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            log("onClosed()", code, reason)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            log("onClosing()", code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            log("onClosing()", t, response ?: "no response")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            log("onMessage1()", "server send msg:", bytes.utf8())
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            log("onMessage2()", "server send msg:", text)
        }
    }
    private val okHttpCliet = OkHttpClient.Builder().build()
    lateinit var newWebSocket: WebSocket

    @SuppressLint("SetTextI18n")
    private fun log(vararg msg: Any) {
        Log.e("Better", "okhttp websocket: ${msg.joinToString(",")}")
        runOnUiThread {
            textView2.text = "${textView2.text}\n${msg.joinToString(",")}"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            // build的时候就自动尝试连接了
            newWebSocket = okHttpCliet.newWebSocket(
                    Request.Builder().url("ws://echo.websocket.org").build(),
                    socketListener)
        }
        close.setOnClickListener {
//             java.lang.IllegalArgumentException: Code must be in range [1000,5000): 1
            newWebSocket.close(1000, "client request close connection")
        }
        send.setOnClickListener {
            newWebSocket.send("Hello i am client")
        }
    }
}