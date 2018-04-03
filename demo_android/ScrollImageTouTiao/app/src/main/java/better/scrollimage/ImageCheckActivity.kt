package better.scrollimage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image_check.*

class ImageCheckActivity : AppCompatActivity(), ScrollImageView.OnDismissImage {
    override fun onUIVisibleChange(showOrGone: Boolean) {
    }

    override fun onDismissImage() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_check)
        img_deatil.setImageResource(R.drawable.downloading)
        ima_imgLay.setOnDismissImage(this)
        img_deatil.setOnPhotoTapListener { view, x, y ->
            finish()
        }
    }
}
