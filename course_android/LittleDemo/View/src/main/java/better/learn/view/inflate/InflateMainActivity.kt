package better.learn.view.inflate

import android.content.Intent
import android.os.Bundle
import better.learn.view.databinding.ActivityInflateMainBinding
import better.library.base.ui.BaseActivity

/**
 * Created by better on 2024/4/6 22:01.
 */
class InflateMainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInflateMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.inflateProblem.setOnClickListener {
            startActivity(Intent(this, InflateProblemActivity::class.java))
        }
        binding.inflateBlink.setOnClickListener {
            startActivity(Intent(this, InflateBlinkActivity::class.java))
        }
        binding.inflateRequestFocus.setOnClickListener {
            startActivity(Intent(this, InflateRequestFocusActivity::class.java))
        }
    }
}