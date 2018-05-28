package better.learn.animation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.view.animation.LinearInterpolator
import better.library.RecycleViewLinearDivider
import better.library.base.ui.BaseRvAdapter
import better.library.base.ui.SimpleViewHolder
import better.library.utils.Utils
import kotlinx.android.synthetic.main.activity_layout_animation.*
import kotlinx.android.synthetic.main.item_rc_one.view.*
import java.util.*

class LayoutAnimationActivity : AppCompatActivity() {

    private val MSG_ONE = 10
    private val MSG_TWO = MSG_ONE + 1
    private val MSG_THREE = MSG_TWO + 1
    private val MSG_FOUR = MSG_THREE + 1
    private val handler = android.os.Handler(Handler.Callback { msg ->
        when (msg.what) {
            MSG_ONE -> {

                (anim_layout_inXML.adapter as BaseRvAdapter<String>).addData(getAdapterTxtData())
                anim_layout_inXML.startLayoutAnimation()
            }
            MSG_TWO -> {

                (anim_layout_inCode.adapter as BaseRvAdapter<String>).addData(getAdapterTxtData())

                val layoutAnnotation = LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.slide_right))
                layoutAnnotation.delay = 1f
                layoutAnnotation.interpolator = LinearInterpolator()
                layoutAnnotation.order = LayoutAnimationController.ORDER_NORMAL
                anim_layout_inCode.layoutAnimation = layoutAnnotation
                // no meed
//                anim_layout_inCode.startLayoutAnimation()
            }
            MSG_THREE -> {

                (anim_layout_reverse.adapter as BaseRvAdapter<String>).addData(getAdapterTxtData())

                val layoutAnnotation = LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.slide_left))
                layoutAnnotation.delay = 1f
                layoutAnnotation.interpolator = LinearInterpolator()
                layoutAnnotation.order = LayoutAnimationController.ORDER_REVERSE
                anim_layout_reverse.layoutAnimation = layoutAnnotation
                anim_layout_reverse.startLayoutAnimation()
            }
            MSG_FOUR -> {

                (anim_layout_random.adapter as BaseRvAdapter<String>).addData(getAdapterTxtData())

                val layoutAnnotation = LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.slide_right))
                layoutAnnotation.delay = 1f
                layoutAnnotation.interpolator = LinearInterpolator()
                layoutAnnotation.order = LayoutAnimationController.ORDER_RANDOM
                anim_layout_random.layoutAnimation = layoutAnnotation
                anim_layout_random.startLayoutAnimation()
            }
        }
        true
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_animation)



        initRV(anim_layout_inXML, RvAdapterTxt())
        initRV(anim_layout_inCode, RvAdapterTxt())
        initRV(anim_layout_reverse, RvAdapterTxt())
        initRV(anim_layout_random, RvAdapterTxt())

        show()
    }

    private fun show() {
        Utils.toast(this, "wait")
        handler.sendEmptyMessageDelayed(MSG_ONE, 1000)
        handler.sendEmptyMessageDelayed(MSG_TWO, 1000)
        handler.sendEmptyMessageDelayed(MSG_THREE, 1000)
        handler.sendEmptyMessageDelayed(MSG_FOUR, 1000)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == R.id.menu_retry) {
                show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRV(recyclerView: RecyclerView, rvAdapter: BaseRvAdapter<String>) {

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = rvAdapter
        recyclerView.addItemDecoration(RecycleViewLinearDivider(this, LinearLayoutManager.VERTICAL, R.drawable.divider_gray))
    }


}

fun getAdapterTxtData(): MutableList<String>? {
    val list = ArrayList<String>()

    val random = Random()
    for (i in 0 until 10) {
        list.add(i, "驱蚊器：" + random.nextInt())
    }
    return list
}

class RvAdapterTxt : BaseRvAdapter<String>() {
    override fun onBindViewHolder(holderSimple: SimpleViewHolder, position: Int) {
        holderSimple.itemView.item_rc_txt.text = getItem(position)
    }

    override fun getItemView(): Int = R.layout.item_rc_one
}
