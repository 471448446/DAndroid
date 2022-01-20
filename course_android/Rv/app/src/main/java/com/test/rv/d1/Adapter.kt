package com.test.rv.d1

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.test.rv.BaseAdapter
import com.test.rv.BaseViewHolder
import com.test.rv.databinding.ItemDemo1Binding

class Adapter : BaseAdapter<Int, ItemDemo1Binding>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemDemo1Binding> = BaseViewHolder(
        ItemDemo1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    ).also {
        Log.d("Demo1", "onCreateViewHolder() ${it.hashCode()}")
        val s =
            Thread.currentThread().stackTrace.joinToString("\n") { stackTrace -> stackTrace.className + ":" + stackTrace.methodName }
        Log.d("Demo1", s)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ItemDemo1Binding>, position: Int) {
        if (holder.itemView.tag !is Int) {
            holder.itemView.tag = position
        }
        Log.d(
            "Demo1",
            "Bind() $position holder:${holder.hashCode()} create from index: ${holder.itemView.tag}"
        )
        val s =
            Thread.currentThread().stackTrace.joinToString("\n") { stackTrace -> stackTrace.className + ":" + stackTrace.methodName }
        Log.d("Demo1", "$position: $s")
        holder.binding.title.text = position.toString()
    }
}