package com.test.rv

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<D, T : ViewBinding> : RecyclerView.Adapter<BaseViewHolder<T>>() {
    val listData: MutableList<D> = mutableListOf()

    override fun getItemCount(): Int = listData.size
}

class BaseViewHolder<T : ViewBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)