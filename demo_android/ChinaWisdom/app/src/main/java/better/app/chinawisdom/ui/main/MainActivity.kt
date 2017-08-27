package better.app.chinawisdom.ui.main

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import better.app.chinawisdom.R
import better.app.chinawisdom.config.SettingConfig
import better.app.chinawisdom.data.bean.BookInfoBean
import better.app.chinawisdom.data.bean.Bookbean
import better.app.chinawisdom.data.db.DbManager
import better.app.chinawisdom.support.common.RecycleViewGridDivider
import better.app.chinawisdom.ui.ReadActivity
import better.app.chinawisdom.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer.*
import kotlinx.android.synthetic.main.tool_bar.*
import kotlin.properties.Delegates

class MainActivity : BaseActivity(), OnSelectBookListener, MainListBookInfoAdapter.OnOpenBookChapterListener {

    private var mBookInfoAdapter: MainListBookInfoAdapter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initContent()
        initDrawer()
    }

    override fun onSelectBook(book: Bookbean) {
        main_drawerLay.closeDrawers()
        toolBar.title = book.name
        mBookInfoAdapter.clear()
        DbManager().getBookInfo(book.name) {
            mBookInfoAdapter.list = it
            main_contentLay_list.scrollToPosition(SettingConfig.chapterSelected)
        }
    }

    override fun onOpenBookChapter(book: BookInfoBean) {
//        toast(book.chapterPath)
        ReadActivity.start(this, book.chapterPath)
    }

    private fun initContent() {
        initToolBar()

        mBookInfoAdapter = MainListBookInfoAdapter(listener = this)
        main_contentLay_list.layoutManager = LinearLayoutManager(this)
        main_contentLay_list.adapter = mBookInfoAdapter
        main_contentLay_list.addItemDecoration(RecycleViewGridDivider(mActivity, R.drawable.divider_gray))
        onSelectBook(SettingConfig.books[SettingConfig.bookSelected])
    }

    private fun initDrawer() {
        drawer_list.layoutManager = LinearLayoutManager(this)
        drawer_list.adapter = MainListBookAdapter(SettingConfig.books, this)
        drawer_list.scrollToPosition(SettingConfig.bookSelected)
    }

    private fun initToolBar() {
        setSupportActionBar(toolBar)
        supportActionBar?.title = ""
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, main_drawerLay, toolBar, R.string.open_string, R.string.close_string)
        actionBarDrawerToggle.syncState()
        main_drawerLay.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                actionBarDrawerToggle.onDrawerSlide(drawerView, slideOffset)
            }

            override fun onDrawerOpened(drawerView: View) {
                actionBarDrawerToggle.onDrawerOpened(drawerView)
            }

            override fun onDrawerClosed(drawerView: View) {
                actionBarDrawerToggle.onDrawerClosed(drawerView)
            }

            override fun onDrawerStateChanged(newState: Int) {
                actionBarDrawerToggle.onDrawerStateChanged(newState)
            }
        })
    }
}
