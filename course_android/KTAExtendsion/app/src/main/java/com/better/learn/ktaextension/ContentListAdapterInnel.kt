package com.better.learn.ktaextension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list.*

class ContentListAdapterInnel : RecyclerView.Adapter<ContentListAdapterInnel.Holder>() {
    private val list = mutableListOf<String>().also { l ->
        l.addAll((1..20).map { "position: $it" })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])
        holder.item_list_position.text = list[position]
    }

    class Holder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(s: String) {
            item_list_position.text = s
        }
    }
}