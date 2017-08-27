package better.app.chinawisdom.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Des 统一跳转方式
 * Create By better on 2016/12/26 13:17.
 */
object ForWordUtils {
    val NO_REQUEST_CODE = -1

    fun to(context: Context, des: Class<*>) {
        to(context, des, null, NO_REQUEST_CODE)
    }

    fun to(context: Context, des: Class<*>, ex: Bundle? = null) {
        to(context, des, ex, NO_REQUEST_CODE)
    }

    fun to(context: Context, des: Class<*>, extra: Bundle?, reqCode: Int) {
        val intent = Intent(context, des)
        if (null != extra) {
            intent.putExtras(extra)
        }
        if (context !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (NO_REQUEST_CODE != reqCode) {
            (context as? Activity)?.startActivityForResult(intent, reqCode)
            log("startActivityForResult should pass Activity")
        } else {
            context.startActivity(intent)
        }
    }

    fun to(context: Fragment, des: Class<*>) {
        to(context, des, null, NO_REQUEST_CODE)
    }

    fun to(context: Fragment, des: Class<*>, ex: Bundle) {
        to(context, des, ex, NO_REQUEST_CODE)
    }

    fun to(context: Fragment, des: Class<*>, extra: Bundle? = null, reqCode: Int = NO_REQUEST_CODE) {
        val intent = Intent(context.context, des)
        if (null != extra) {
            intent.putExtras(extra)
        }
        if (NO_REQUEST_CODE != reqCode) {
            context.startActivityForResult(intent, reqCode)
        } else {
            context.startActivity(intent)
        }
    }

    fun service(context: Activity, des: Class<*>, extra: Bundle? = null) {
        val intent = Intent(context, des)
        if (null != extra) {
            intent.putExtras(extra)
        }
        context.startService(intent)
    }

    fun isExistActivity(activity: Context, intent: Intent): Boolean {
        return null != intent.resolveActivity(activity.packageManager)
    }
}
