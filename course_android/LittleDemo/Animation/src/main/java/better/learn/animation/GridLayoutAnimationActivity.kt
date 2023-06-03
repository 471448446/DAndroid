package better.learn.animation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import better.library.base.ui.BaseAdapter
import kotlinx.android.synthetic.main.activity_grid_layout_animation.*
import kotlinx.android.synthetic.main.item_rc_one.view.*


/**
 * https://gist.github.com/brucetoo/23ce1a375a32b28d6d3a#file-layoutanimation-java-L42
 *
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/0819/8397.html
 */
class GridLayoutAnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_layout_animation)

        gridLayoutAnimationRc.layoutManager = GridLayoutManager(this, 3)
        val adapterTxt = RvAdapterTxt()
        adapterTxt.addData(getAdapterTxtData())
        gridLayoutAnimationRc.adapter = adapterTxt

        // no need
//        gridLayoutAnimationRc.startLayoutAnimation()
//        gridLayoutAnimationRc.scheduleLayoutAnimation()

        val adapter = GridAdapter(this)
        adapter.addData(getAdapterTxtData())
        gridLayoutAnimationGrid.adapter = adapter
    }

    class GridAdapter(ctx: Context) : BaseAdapter<String>(ctx) {
        override fun initData(holder: ViewHolder?, position: Int) {
            holder?.itemView?.item_rc_txt?.text = getItem(position)
        }

        override fun getViewId(): Int = R.layout.item_rc_one

    }

}

