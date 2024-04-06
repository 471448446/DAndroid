package better.learn.view.inflate

import android.os.Bundle
import better.learn.view.databinding.ActivityInflateBlinkBinding
import better.library.base.ui.BaseActivity

/**
 * Created by better on 2024/4/6 22:06.
 */
class InflateBlinkActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInflateBlinkBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}