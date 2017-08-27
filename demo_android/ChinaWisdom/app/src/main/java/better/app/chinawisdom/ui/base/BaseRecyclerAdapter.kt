package better.app.chinawisdom.ui.base

import android.support.v7.widget.RecyclerView

/**
 * Created by better on 2017/8/20 09:43.
 */
abstract class BaseRecyclerAdapter<T, H : RecyclerView.ViewHolder?>(data: List<T>) : RecyclerView.Adapter<H>() {
    private var list: List<T> = data
    override fun getItemCount(): Int = list.size

    fun getItem(p: Int): T = list[p]

    fun setData(list: List<T>): BaseRecyclerAdapter<T, H> {
        this.list = list
        notifyDataSetChanged()
        return this
    }

}