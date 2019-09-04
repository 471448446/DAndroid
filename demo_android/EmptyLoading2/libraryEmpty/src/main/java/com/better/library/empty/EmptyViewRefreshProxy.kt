package com.better.library.empty

import android.view.View

interface EmptyViewRefreshProxy {

    val proxyView: View

    fun displayLoading(msg: Int)

    /**
     * 显示请求等待。
     *
     * @param msg 等待提示消息。
     */
    fun displayLoading(msg: String = proxyView.context.getString(R.string.empty_tip_loading))

    fun displayMessage(msg: Int)

    /**
     * 请求成功 当列表为空的时候展示的信息
     * Create By better on 2017/4/2 07:14.
     */
    fun displayMessage(msg: String = proxyView.context.getString(R.string.empty_tip_empty))

    fun displayRetry(msg: Int)

    /**
     * 请求失败 显示请求重试。
     *
     * @param msg 重试提示消息：失败、无数据。
     */
    fun displayRetry(msg: String = proxyView.context.getString(R.string.empty_tip_fail))

    /**
     * 直接消失emptyView
     */
    fun disappear()

    fun setOnRetryClickListener(listener: OnLrpRetryClickListener)
}
