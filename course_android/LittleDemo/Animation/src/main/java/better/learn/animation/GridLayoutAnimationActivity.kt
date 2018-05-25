package better.learn.animation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_grid_layout_animation.*


/**
 * https://gist.github.com/brucetoo/23ce1a375a32b28d6d3a#file-layoutanimation-java-L42
 *
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/0819/8397.html
 */
class GridLayoutAnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_layout_animation)

        gridLayoutAnimation.layoutManager = GridLayoutManager(this, 3)
        val adapterTxt = AdapterTxt()
        adapterTxt.addData(getAdapterTxtData())
        gridLayoutAnimation.adapter = adapterTxt

        gridLayoutAnimation.scheduleLayoutAnimation()
    }
}

