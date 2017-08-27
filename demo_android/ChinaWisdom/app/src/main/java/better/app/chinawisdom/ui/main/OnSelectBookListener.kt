package better.app.chinawisdom.ui.main

import better.app.chinawisdom.data.bean.Bookbean

/**
 * 如果不用Lambda就要写一个回调事件
 * Created by better on 2017/8/20 10:00.
 */
interface OnSelectBookListener {
    fun onSelectBook(book: Bookbean)
}