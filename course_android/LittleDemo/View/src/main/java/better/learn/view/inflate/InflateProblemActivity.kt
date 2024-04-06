package better.learn.view.inflate

import android.os.Bundle
import android.view.LayoutInflater
import better.learn.view.databinding.ActivityInflateViewBinding
import better.learn.view.databinding.ViewInflateItemBinding
import better.library.base.ui.BaseActivity

/**
 * Created by better on 2024/4/5 22:14.
 */
class InflateProblemActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LayoutInflater.from(this)
        val binding = ActivityInflateViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // inflate的时候指定父容器，子view的高度符合预期，50dp
        ViewInflateItemBinding.inflate(layoutInflater, binding.root, false).also {
            binding.top.addView(it.root)
        }
        // inflate的时候不指定父容器，子view的高度不符合预期
        // inflate的方法注释中说到，提供的父容器用于设置被填充view的 LayoutParams
        ViewInflateItemBinding.inflate(layoutInflater).also {
            binding.bottom.addView(it.root)
        }

    }
}