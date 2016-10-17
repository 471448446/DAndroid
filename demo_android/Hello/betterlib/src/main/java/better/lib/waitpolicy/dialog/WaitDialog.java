package better.lib.waitpolicy.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import better.lib.R;

/**
 * Des 当前Activity 创建了就不创建,没创建就创建。
 * 每个Activity拥有自己的WaitDialog
 * Create By better on 2016/9/27 22:10.
 */
public class WaitDialog extends Dialog {

    private WaitDialog(Context context, int theme) {
        super(context, theme);
    }

    //用于销毁
    private WaitDialog(Context context) {
        super(context);
    }

    private static WaitDialog waitDialog = null;
    private static Context mContext;

    public static WaitDialog getInstance(Context context) {
        return getInstance(context, false);
    }

    /**
     * 因需要可以取消的waitDialog 所以改范围private为public  151215
     */
    public static WaitDialog getInstance(Context context, boolean isCancelable) {
        if (waitDialog == null || context != mContext) {
            mContext = context;
            View view = View.inflate(mContext, R.layout.loading, null);
            waitDialog = new WaitDialog(mContext, R.style.alert_dialog_style);
            waitDialog.setContentView(view);
            waitDialog.setCancelable(isCancelable);
        }
        return waitDialog;
    }

    public static WaitDialog getDestroyAction(Context context) {
        return new WaitDialog(context);
    }

    public void showWaitDialog(int msgId) {
        showWaitDialog(mContext.getString(msgId));
    }

    public void showWaitDialog() {
        showWaitDialog(mContext.getString(R.string.str_loading_wait));
    }

    public void showWaitDialog(String msg) {
        TextView msgTv = (TextView) waitDialog.findViewById(R.id.loading_msg);
        msgTv.setText(msg);
        if (!waitDialog.isShowing()) {
            waitDialog.show();
        }
    }

    public void dismissWaitDialog() {
        if (null != waitDialog && waitDialog.isShowing()) {
            waitDialog.dismiss();
            mContext = null;
//            waitDialog = null;
        }
    }

    // 不确定显示没有就调用这个 是否Context
    public static void onDestroyAction(Context context) {
        if (null != mContext && mContext == context/*创建了没有dismiss*/) mContext = null;
    }
}
