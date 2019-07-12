package com.better.learn.rvitempos

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val margin = findViewById<View>(R.id.margin)

        findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            post {
                val rect = Rect()
                window.decorView.getWindowVisibleDisplayFrame(rect)
                adapter = Adapter(margin, rect.top)
            }
        }
    }
}

class Adapter(val view: View, val statusHeight: Int) : RecyclerView.Adapter<Holder>() {
    private val date = (0 until 20).map { "pos $it" }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false))

    override fun getItemCount() = date.size

    override fun onBindViewHolder(holder: Holder, p: Int) {
        holder.itemView.apply {
            setOnClickListener {
                val loc = IntArray(2) { 0 }
                it.getLocationInWindow(loc)
                Log.e("Better", "__${loc.joinToString(",")}")
                val param = view.layoutParams as ConstraintLayout.LayoutParams
                param.topMargin = loc[1] - statusHeight
                view.layoutParams = param
            }
            findViewById<TextView>(R.id.text).text = date[p]
        }
    }
}

class Holder(v: View) : RecyclerView.ViewHolder(v)
