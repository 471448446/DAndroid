package better.app.chinawisdom.support.extenions

import android.graphics.Typeface
import android.support.v7.widget.Toolbar
import android.widget.TextView
import better.app.chinawisdom.SettingConfig

/**
 * Created by better on 2017/9/7 16:52.
 */
fun Toolbar.setTypeFace(face: Typeface) {
    val toolbarReflect = Toolbar::class.java.getDeclaredField("mTitleTextView")
    toolbarReflect.isAccessible = true
    val titleTextView: TextView = toolbarReflect.get(this) as TextView
    titleTextView.typeface = SettingConfig.configTextFace
}
