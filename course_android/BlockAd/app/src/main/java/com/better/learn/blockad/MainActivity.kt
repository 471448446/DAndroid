package com.better.learn.blockad

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayInputStream


class MainActivity : AppCompatActivity() {
    companion object {
        val novel1 = "xbiquge.la" to "http://www.xbiquge.la/"
        val novel2 = "shuquge.com" to " http ://www.shuquge.com/"
        val novel3 = "zuopinj.com" to "http://zuopinj.com/"
        val novel4 = "xbiquge.la" to "http://m.xbiquge.la/"
        /*
        https://img.wusnz.cn/ss-x7/2019/11/0/1573711764153.gif
         */
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val novelUrl = "http://www.baidu.com"
//        method1()
//        method2()
//        method3()
//        method4()
//        method5()
    }

    /**
     * not work
     */
    private fun method4() {
        val empty = ByteArrayInputStream("".toByteArray())
        webview.apply {
            settings.let { set ->
                set.javaScriptEnabled = true
                set.userAgentString =
                    "Mozilla/5.0 (Linux; Android 8.0.0; AUM-AL20) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.116 Mobile Safari/537.36"
            }

            webViewClient = object : WebViewClient() {
                override fun shouldInterceptRequest(
                    view: WebView?,
                    url: String?
                ): WebResourceResponse? {

                    if (arrayOf(".jpg", ".png", ".webp", ".gif")
                            .map { url?.contains(it) == true }
                            .any { it }
                    ) {
                        return WebResourceResponse(
                            "text/plain",
                            "utf-8",
                            empty
                        )
                    }
                    return super.shouldInterceptRequest(view, url)
                }
            }

            WebView.setWebContentsDebuggingEnabled(true)

            loadUrl(novel4.second)
        }
    }

    /**
     * 根据 domain 过滤？
     */
    private fun method5() {
        val empty = ByteArrayInputStream("".toByteArray())

        webview.apply {
            settings.let { set ->
                set.javaScriptEnabled = true

                set.userAgentString =
                    "Mozilla/5.0 (Linux; Android 8.0.0; AUM-AL20) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.116 Mobile Safari/537.36"
            }

            webViewClient = object : WebViewClient() {
                override fun shouldInterceptRequest(
                    view: WebView?,
                    url: String?
                ): WebResourceResponse? {
                    if (url?.contains("wusnz.cn") == true) {
                        Log.e("AdBlock", "block $url")
                        return WebResourceResponse(
                            "text/plain",
                            "utf-8",
                            empty
                        )
                    }
                    return super.shouldInterceptRequest(view, url)
                }
            }

            WebView.setWebContentsDebuggingEnabled(true)

            loadUrl(novel4.second)
        }
    }

    /**
     * partial work
     * 图片是拦截了，广告也拦截了，但是点击页面还是会自动跳转
     * 这种方式整个img 标签都不占位了
     */
    private fun method3() {
        webview.apply {
            settings.let { set ->
                set.javaScriptEnabled = true

                set.userAgentString =
                    "Mozilla/5.0 (Linux; Android 8.0.0; AUM-AL20) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.116 Mobile Safari/537.36"
                set.loadsImagesAutomatically = false
                set.blockNetworkImage = true
            }

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    view?.loadUrl(
                        "javascript:(function(){ var imgs=document.getElementsByTagName('img');" +
                                "for(i=0;i<imgs.length;i++) { imgs[i].style.display='none'; } })()"
                    )
                }
            }

            WebView.setWebContentsDebuggingEnabled(true)

            loadUrl(novel4.second)
        }
    }

    /**
     * partial worked
     * 图片和广告图都没有加载，不过广告的点击位置还在。那块广告区域，只要点击了还是可以触发广告。
     * 这个是不加载图片，但是图片位置还是在，显示的是一个加载失败的占位图
     */
    private fun method2() {
        webview.apply {

            settings.let { set ->
                set.javaScriptEnabled = true

                set.userAgentString =
                    "Mozilla/5.0 (Linux; Android 8.0.0; AUM-AL20) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.116 Mobile Safari/537.36"
                set.loadsImagesAutomatically = false
                set.blockNetworkImage = true
            }

            webViewClient = object : WebViewClient() {
            }

            WebView.setWebContentsDebuggingEnabled(true)

            loadUrl(novel4.second)
        }
    }

    /**
     * partial worked
     * 能看到广告还是在跳转，虽然没有加载出来图片
     * 这个是不加载图片，但是图片位置还是在，显示的是一个加载失败的放占位图
     */
    private fun method1() {
        webview.apply {

            settings.let { set ->
                set.javaScriptEnabled = true

                set.userAgentString =
                    "Mozilla/5.0 (Linux; Android 8.0.0; AUM-AL20) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.116 Mobile Safari/537.36"
                set.loadsImagesAutomatically = false
            }

            webViewClient = object : WebViewClient() {
            }

            WebView.setWebContentsDebuggingEnabled(true)

            loadUrl(novel4.second)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return webview.canGoBack().also {
            if (it) {
                webview.goBack()
            }
        } || super.onKeyDown(keyCode, event)
    }
}
