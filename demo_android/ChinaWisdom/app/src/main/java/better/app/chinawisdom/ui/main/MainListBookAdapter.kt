package better.app.chinawisdom.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import better.app.chinawisdom.R
import better.app.chinawisdom.config.SettingConfig
import better.app.chinawisdom.data.bean.Bookbean
import better.app.chinawisdom.ui.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.item_drawer_list_book.view.*


/**
 * Created by better on 2017/8/20 09:42.
 */
class MainListBookAdapter(data: List<Bookbean> = arrayListOf(), private val listener: OnSelectBookListener) :
        BaseRecyclerAdapter<Bookbean, MainListBookAdapter.Holder>(data) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_drawer_list_book, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val bean = getItem(position)
        holder.itemView.item_drawerList_txt.text = bean.name
        if (SettingConfig.bookSelected == position) {
            holder.itemView.item_drawerList_txt.setBackgroundResource(R.color.colorGray_select)
        } else {
            holder.itemView.item_drawerList_txt.setBackgroundResource(R.color.colorWhite)
        }
        holder.itemView.setOnClickListener {
            if (SettingConfig.bookSelected != position) {
                SettingConfig.rememberBookSelect(position)
                notifyDataSetChanged()
                listener.onSelectBook(bean)
            }else{
                listener.closeDrawer()
            }
        }
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view)
}