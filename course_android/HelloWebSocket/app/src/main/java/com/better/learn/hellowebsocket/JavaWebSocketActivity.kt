package com.better.learn.hellowebsocket

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

/**
 * https://github.com/TooTallNate/Java-WebSocket
 * JavaWebSocket 使用：
 * https://juejin.im/post/6844903577438126094
 * https://blog.csdn.net/xch_yang/article/details/88888350
 * SSL:
 * https://stackoverflow.com/questions/36150164/unable-to-connect-websocket-with-wss-in-android
 * @author Better
 * @date 2020/7/31 11:17
 */
class JavaWebSocketActivity : AppCompatActivity() {

    private val webSocketClient =
        object : WebSocketClient(URI("ws://echo.websocket.org"), Draft_6455(), null) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                log("onOpen()", handshakedata?.run {
                    "$httpStatus,$httpStatusMessage"
                } ?: " server no response ServerHandshake")
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                log("onClose", code, reason ?: "no reason", "is remote?", remote)
            }

            override fun onMessage(message: String?) {
                log("onMessage()", message ?: "no message")
            }

            override fun onError(ex: Exception?) {
                log("onError()", ex ?: "no Exception")
            }

        }

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
            // 必须调用才发起连接
            webSocketClient.connect()
        }
        close.setOnClickListener {
            webSocketClient.close(1000)
        }
        send.setOnClickListener {
            webSocketClient.send("Hello i am from client")
        }
    }
}