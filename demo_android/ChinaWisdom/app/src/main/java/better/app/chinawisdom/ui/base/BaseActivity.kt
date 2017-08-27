package better.app.chinawisdom.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlin.properties.Delegates

/**
 * Created by better on 2017/8/17 11:35.
 */
open class BaseActivity : AppCompatActivity() {
    var mActivity: AppCompatActivity by Delegates.notNull()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
    }
}