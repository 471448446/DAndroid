package better.learn.view.custom.canvas.compose.xfermode

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import better.learn.view.databinding.ActivityCanvasXfermodeBinding
import better.learn.view.databinding.ItemCanvasXfermodeBinding
import better.library.base.ui.SimpleAdapter
import better.library.base.ui.SimpleHolder

/**
 * 图层混合模式使用的三种场景：1.ComposeShader（混合渲染）；2.画笔的Paint.setXfermode（）；3.PorterDuffColorFilter（颜色过滤器）。
 * Created by better on 2023/6/3 17:49.
 */
class CanvasXFermodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCanvasXfermodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = listOf(

            Item(
                "src",
                //显示上层绘制的图像
                PorterDuff.Mode.SRC
            ),
            Item(
                "dst",
                //显示下层绘制图像
                PorterDuff.Mode.DST
            ),
            Item(
                "srcOver",
                //正常绘制显示，上下层绘制叠盖
                PorterDuff.Mode.SRC_OVER
            ),
            Item(
                "dstOver",
                //上下层都显示，下层居上显示
                PorterDuff.Mode.DST_OVER
            ),
            Item(
                "srcIn",
                //取两层绘制交集，显示上层
                PorterDuff.Mode.SRC_IN
            ),
            Item(
                "dstIn",
                //取两层绘制交集，显示下层
                PorterDuff.Mode.DST_IN
            ),
            Item(
                "scrOut",
                //取上层绘制非交集部分，交集部分变成透明
                PorterDuff.Mode.SRC_OUT
            ),
            Item(
                "dstOut",
                //取下层绘制非交集部分，交集部分变成透明
                PorterDuff.Mode.DST_OUT
            ),
            Item(
                "scrATop",
                //取上层交集部分与下层非交集部分
                PorterDuff.Mode.SRC_ATOP
            ),
            Item(
                "dstATop",
                //取下层交集部分与上层非交集部分
                PorterDuff.Mode.DST_ATOP
            ),
            Item(
                "clear",
                //所绘制不会提交到画布上
                PorterDuff.Mode.CLEAR
            ),
            Item(
                "xor",
                //去除两图层交集部分
                PorterDuff.Mode.XOR
            ),
            Item(
                "darken",
                //取两图层全部区域，交集部分颜色加深
                PorterDuff.Mode.DARKEN
            ),
            Item(
                "lighten",
                //取两图层全部区域，交集部分颜色点亮
                PorterDuff.Mode.LIGHTEN
            ),
            Item(
                "multiply",
                //取两图层交集部分，颜色叠加
                PorterDuff.Mode.MULTIPLY
            ),
            Item(
                "screen",
                //取两图层全部区域，交集部分滤色
                PorterDuff.Mode.SCREEN
            ),
            Item(
                "add",
                //取两图层全部区域，交集部分饱和度相加
                PorterDuff.Mode.ADD
            ),
            Item(
                "overlay",
                //取两图层全部区域，交集部分叠加
                PorterDuff.Mode.OVERLAY
            ),
        )
        binding.root.apply {
            layoutManager = GridLayoutManager(this@CanvasXFermodeActivity, 2)
            adapter = Adapter().also {
                it.addData(list)
            }
        }

    }

    data class Item(
        val title: String,
        val mode: PorterDuff.Mode
    )

    class Adapter : SimpleAdapter<Item, SimpleHolder<ItemCanvasXfermodeBinding>>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SimpleHolder<ItemCanvasXfermodeBinding> = SimpleHolder(
            ItemCanvasXfermodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        override fun onBindViewHolder(
            holder: SimpleHolder<ItemCanvasXfermodeBinding>,
            position: Int
        ) {
            val bean = getItem(position)
            holder.binding.title.apply {
                text = bean.title
            }
            holder.binding.xfermode.apply {
                xfermode = bean.mode
            }
        }

    }
}