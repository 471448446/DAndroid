package better.app.chinawisdom.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import better.app.chinawisdom.R
import better.app.chinawisdom.config.SettingConfig
import better.app.chinawisdom.data.bean.BookInfoBean
import better.app.chinawisdom.util.log
import kotlinx.android.synthetic.main.item_chapter.view.*
import kotlinx.android.synthetic.main.item_chapter_name.view.*

/**
 * Created by better on 2017/8/20 10:22.
 */
class MainListBookInfoAdapter(data: List<BookInfoBean> = arrayListOf(), private val listener: OnOpenBookChapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        val CHAPTER_NAME = 1
        val CHAPTER = CHAPTER_NAME + 1
    }

    interface OnOpenBookChapterListener {
        fun onOpenBookChapter(book: BookInfoBean)
    }

    var list: List<BookInfoBean> = data
        set(value) {
            field = value
            log("改变书本目录" + field)
            notifyDataSetChanged()
        }

    fun clear() {
        this.list = arrayListOf()
    }

    override fun getItemCount(): Int = list.size
    override fun getItemViewType(position: Int): Int =
            if (list[position].chapterPath.isEmpty()) CHAPTER_NAME else CHAPTER

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = list[position]
        if (holder is HolderChapter) {
            holder.itemView.item_chapter_txt.text = bean.chapter
            if (SettingConfig.chapterSelected == position) {
                holder.itemView.item_chapter_txt.setBackgroundResource(R.color.colorGray_select)
            } else {
                holder.itemView.item_chapter_txt.setBackgroundResource(R.color.colorWhite)
            }
            holder.itemView.setOnClickListener {
                SettingConfig.rememberBookChapterSelect(position)
                notifyDataSetChanged()
                listener.onOpenBookChapter(bean)
            }
        } else {
            holder.itemView.item_chapterName_txt.text = bean.chapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            if (CHAPTER == viewType) HolderChapter(LayoutInflater.from(parent.context).inflate(R.layout.item_chapter, parent, false))
            else HolderChapterName(LayoutInflater.from(parent.context).inflate(R.layout.item_chapter_name, parent, false))

    class HolderChapterName(view: View) : RecyclerView.ViewHolder(view)

    class HolderChapter(view: View) : RecyclerView.ViewHolder(view)
}