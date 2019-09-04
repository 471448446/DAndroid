package com.better.library.empty

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.get

class LoadingProxy(private val wrapper: FrameLayout) : EmptyViewRefreshProxy {
    companion object {
        fun wrap(view: View): LoadingProxy {
            val wrapper = FrameLayout(view.context)
            if (null == view.parent) {
                wrapper.layoutParams = view.layoutParams
            } else {
                val parent = view.parent as ViewGroup
                var index = 0
                for (i in (0 until parent.childCount)) {
                    if (parent[i] == view) {
                        index = i
                        break
                    }
                }
                parent.removeView(view)
                parent.addView(wrapper, index, view.layoutParams)
            }
            wrapper.addView(view)
            return LoadingProxy(wrapper)
        }
    }

    private var emptyProxy = EmptyViewProxyImpl(wrapper.context)
        set(value) {
            val params = field.proxyView.layoutParams as ViewGroup.LayoutParams
            wrapper.removeView(field.proxyView)
            field = value
            wrapper.addView(field.proxyView, params)
        }

    override val proxyView: View
        get() = wrapper

    init {
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        wrapper.addView(emptyProxy.proxyView, params)
    }

    override fun displayLoading(msg: Int) {
        emptyProxy.displayLoading(msg)
    }

    /**
     * 显示请求等待。
     *
     * @param msg 等待提示消息。
     */
    override fun displayLoading(msg: String) {
        emptyProxy.displayLoading(msg)
    }

    override fun displayMessage(msg: Int) {
        emptyProxy.displayMessage(msg)
    }

    /**
     * 请求成功 当列表为空的时候展示的信息
     * Create By better on 2017/4/2 07:14.
     */
    override fun displayMessage(msg: String) {
        emptyProxy.displayMessage(msg)
    }

    override fun displayRetry(msg: Int) {
        emptyProxy.displayRetry(msg)
    }

    /**
     * 请求失败 显示请求重试。
     *
     * @param msg 重试提示消息：失败、无数据。
     */
    override fun displayRetry(msg: String) {
        emptyProxy.displayRetry(msg)
    }

    /**
     * 直接消失emptyView
     */
    override fun disappear() {
        emptyProxy.disappear()
    }

    override fun setOnRetryClickListener(listener: OnLrpRetryClickListener) {
        emptyProxy.setOnRetryClickListener(listener)
    }

}