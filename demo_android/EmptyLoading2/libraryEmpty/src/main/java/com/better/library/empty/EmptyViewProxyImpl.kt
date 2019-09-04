package com.better.library.empty

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.empty_loading_retry.view.*
import kotlinx.android.synthetic.main.empty_retry.view.*

class EmptyViewProxyImpl(context: Context) : EmptyViewRefreshProxy {

    @SuppressLint("InflateParams")
    override val proxyView: View =
        LayoutInflater.from(context).inflate(R.layout.empty_loading_retry, null, false)

    private lateinit var onLrpRetryClickListener: OnLrpRetryClickListener

    override fun displayLoading(msg: Int) {
        displayLoading(proxyView.context.getString(msg))
    }

    /**
     * 显示请求等待。
     *
     * @param msg 等待提示消息。
     */
    override fun displayLoading(msg: String) {
        changeVisibility(root = true, loading = true, retry = false)
        proxyView.empty_loading_msg.text = msg
    }

    override fun displayMessage(msg: Int) {
        displayMessage(proxyView.context.getString(msg))
    }

    /**
     * 请求成功 当列表为空的时候展示的信息
     * Create By better on 2017/4/2 07:14.
     */
    override fun displayMessage(msg: String) {
        changeVisibility(root = true, loading = false, retry = true)
        proxyView.empty_retry_message.text = msg
        if (::onLrpRetryClickListener.isInitialized) {
            proxyView.empty_retry_lay.setOnClickListener {
                onLrpRetryClickListener.onRetryClick()
            }
        }
    }

    override fun displayRetry(msg: Int) {
        displayRetry(proxyView.context.getString(msg))
    }

    /**
     * 请求失败 显示请求重试。
     *
     * @param msg 重试提示消息：失败、无数据。
     */
    override fun displayRetry(msg: String) {
        changeVisibility(root = true, loading = false, retry = true)
        proxyView.empty_retry_message.text = msg
        if (::onLrpRetryClickListener.isInitialized) {
            proxyView.empty_retry_lay.setOnClickListener {
                onLrpRetryClickListener.onRetryClick()
            }
        }
    }

    /**
     * 直接消失emptyView
     */
    override fun disappear() {
        changeVisibility(root = false, loading = false, retry = false)
    }

    override fun setOnRetryClickListener(listener: OnLrpRetryClickListener) {
        onLrpRetryClickListener = listener
    }

    private fun changeVisibility(root: Boolean, loading: Boolean, retry: Boolean) {
        if (root) {
            proxyView.visibility = View.VISIBLE
            // loading
            proxyView.empty_loading_progress.visibility =
                if (loading) View.VISIBLE else View.GONE
            proxyView.empty_loading_msg.visibility =
                if (loading) View.VISIBLE else View.GONE
            // retry
            if (retry) {
                if (null == proxyView.empty_retry_lay) {
                    proxyView.empty_retry_stub.inflate()
                }
                proxyView.empty_retry_lay.visibility = View.VISIBLE
            } else {
                if (null != proxyView.empty_retry_lay) {
                    proxyView.empty_retry_lay.visibility = View.GONE
                }
            }
        } else {
            proxyView.visibility = View.GONE
        }

    }

}