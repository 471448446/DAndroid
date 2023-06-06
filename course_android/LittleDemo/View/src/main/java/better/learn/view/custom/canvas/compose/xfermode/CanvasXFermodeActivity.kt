package better.learn.view.custom.canvas.compose.xfermode

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import better.learn.view.databinding.ActivityCanvasXfermodeBinding
import better.learn.view.databinding.ItemCanvasXfermode2Binding
import better.learn.view.databinding.ItemCanvasXfermodeBinding
import better.library.base.ui.SimpleAdapter
import better.library.base.ui.SimpleHolder

/**
 * 不带透明通道vs带有通明通道
 * 图层混合模式使用的三种场景：1.ComposeShader（混合渲染）；2.画笔的Paint.setXfermode（）；3.PorterDuffColorFilter（颜色过滤器）。
 * Created by better on 2023/6/3 17:49.
 */
// 这个是直接绘制的View组合，发现跟结论有点冲突，比如SRC、SRC_IN、SRC_OUT
// 然后用bitmap进行组合，跟结论对上了
class CanvasXFermodeActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCanvasXfermodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val result = createListData()
        binding.title.text = "非Bitmap进行混合"
        binding.list.apply {
            layoutManager = GridLayoutManager(this@CanvasXFermodeActivity, 5)
            adapter = Adapter().also {
                it.addData(result)
            }
        }

    }

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
            if (null != bean.mode) {
                holder.binding.title.apply {
                    text = bean.name()
                }
                holder.binding.xfermode.apply {
                    xfermode = bean.mode
                    useAlpha = bean.alpha
                }
            } else {
                // 文本
                holder.binding.title.text = ""
                holder.binding.xfermode.apply {
                    xfermode = null
                    useAlpha = bean.alpha
                }
            }
        }

    }
}

class CanvasXFermodeBitmapActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCanvasXfermodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.title.text = "大小一致的Bitmap进行混合"
        val result = createListData()
        binding.list.apply {
            layoutManager = GridLayoutManager(this@CanvasXFermodeBitmapActivity, 5)
            adapter = Adapter().also {
                it.addData(result)
            }
        }

    }

    class Adapter : SimpleAdapter<Item, SimpleHolder<ItemCanvasXfermode2Binding>>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SimpleHolder<ItemCanvasXfermode2Binding> = SimpleHolder(
            ItemCanvasXfermode2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        override fun onBindViewHolder(
            holder: SimpleHolder<ItemCanvasXfermode2Binding>,
            position: Int
        ) {
            val bean = getItem(position)
            if (null != bean.mode) {
                holder.binding.title.apply {
                    text = bean.name()
                }
                holder.binding.xfermode.apply {
                    xfermode = bean.mode
                    useAlpha = bean.alpha
                }
            } else {
                // 文本
                holder.binding.title.text = ""
                holder.binding.xfermode.apply {
                    xfermode = null
                    useAlpha = bean.alpha
                }
            }
        }

    }
}

fun createListData(): List<Item> {
    val porterDuff = listOf(
        PorterDuff.Mode.SRC,
        PorterDuff.Mode.DST,
        PorterDuff.Mode.SRC_OVER,
        PorterDuff.Mode.DST_OVER,
        PorterDuff.Mode.SRC_IN,
        PorterDuff.Mode.DST_IN,
        PorterDuff.Mode.SRC_OUT,
        PorterDuff.Mode.DST_OUT,
        PorterDuff.Mode.SRC_ATOP,
        PorterDuff.Mode.DST_ATOP,
        PorterDuff.Mode.CLEAR,
        PorterDuff.Mode.XOR,
        //
        PorterDuff.Mode.ADD,
        PorterDuff.Mode.DARKEN,
        PorterDuff.Mode.LIGHTEN,
        PorterDuff.Mode.MULTIPLY,
        PorterDuff.Mode.SCREEN,
        PorterDuff.Mode.OVERLAY,
    )
    var useAlpha = false
    val result = (porterDuff + listOf(null, null) + porterDuff).map {
        // 前面的一匹不用Alpha，后面的一批使用Alpha
        if (it == null && !useAlpha) {
            useAlpha = true
        }
        Item(useAlpha, it)
    }
    return result
}

data class Item(
    // 使用alpha的颜色
    val alpha: Boolean,
    val mode: PorterDuff.Mode?
) {
    fun name(): String = when (mode) {
        //所绘制不会提交到画布上
        PorterDuff.Mode.CLEAR -> "clear"
        //显示上层绘制的图像
        PorterDuff.Mode.SRC -> "src"
        //显示下层绘制图像
        PorterDuff.Mode.DST -> "dst"
        //正常绘制显示，上下层绘制叠盖
        PorterDuff.Mode.SRC_OVER -> "srcOver"
        //上下层都显示，下层居上显示
        PorterDuff.Mode.DST_OVER -> "dstOver"
        //取两层绘制交集，显示上层
        PorterDuff.Mode.SRC_IN -> "srcIn"
        //取两层绘制交集，显示下层
        PorterDuff.Mode.DST_IN -> "dstIn"
        //取上层绘制非交集部分，交集部分变成透明
        PorterDuff.Mode.SRC_OUT -> "srcOut"
        //取下层绘制非交集部分，交集部分变成透明
        PorterDuff.Mode.DST_OUT -> "dstOut"
        //取上层交集部分与下层非交集部分
        PorterDuff.Mode.SRC_ATOP -> "srcAtop"
        //取下层交集部分与上层非交集部分
        PorterDuff.Mode.DST_ATOP -> "dstAtop"
        //去除两图层交集部分
        PorterDuff.Mode.XOR -> "xor"
        //取两图层全部区域，交集部分颜色加深
        PorterDuff.Mode.DARKEN -> "darken"
        //取两图层全部区域，交集部分颜色点亮
        PorterDuff.Mode.LIGHTEN -> "light"
        //取两图层交集部分，颜色叠加
        PorterDuff.Mode.MULTIPLY -> "multiply"
        //取两图层全部区域，交集部分滤色
        PorterDuff.Mode.SCREEN -> "screen"
        //取两图层全部区域，交集部分饱和度相加
        PorterDuff.Mode.ADD -> "add"
        //取两图层全部区域，交集部分叠加
        PorterDuff.Mode.OVERLAY -> "overlay"
        else -> {
            ""
        }
    }
}
