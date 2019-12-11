package com.better.learn.kt.ktap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.better.learn.kt.R
import kotlinx.android.synthetic.main.item_list.view.*

class ContentListAdapter : RecyclerView.Adapter<ContentListAdapter.Holder>() {
    private val list = mutableListOf<String>().also { l ->
        l.addAll((1..20).map { "position: $it" })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list,
                parent,
                false
            )
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.item_list_position.text = list[position]
    }

    class Holder(v: View) : RecyclerView.ViewHolder(v)
}