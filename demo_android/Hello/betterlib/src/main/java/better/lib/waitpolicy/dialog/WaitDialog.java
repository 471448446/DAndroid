package better.lib.waitpolicy.dialog;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import better.lib.R;

/**
 * Des 当前Activity 创建了就不创建,没创建就创建。
 * 每个Activity拥有自己的WaitDialog
 * Create By better on 2016/9/27 22:10.
 */
public class WaitDialog extends Dialog {

    private static Map<String, WaitDialog> listWaitDialog = new HashMap<>();

    private WaitDialog(Context context, int theme) {
        super(context, theme);
    }

    private WaitDialog(Context context) {
        super(context);
    }

    public static WaitDialog getInstance(Context context) {
        return getInstance(context, false);
    }

    /**
     * 因需要可以取消的waitDialog 所以改范围private为public  151215
     */
    public static WaitDialog getInstance(@NonNull Context context, boolean isCancelable) {
        final String key = context.getClass().getSimpleName();
        if (null == listWaitDialog.get(key)) {
            synchronized (WaitDialog.class) {
                if (null == listWaitDialog.get(key)) {
                    View view = View.inflate(context, R.layout.loading, null);
                    WaitDialog dialog = new WaitDialog(context, R.style.alert_dialog_style);
                    dialog.setContentView(view);
                    dialog.setCancelable(isCancelable);
                    listWaitDialog.put(key, dialog);
                }
            }
        }
        return listWaitDialog.get(key);
    }

    public void showWaitDialog(int msgId) {
        showWaitDialog(getContext().getString(msgId));
    }

    public void showWaitDialog() {
        showWaitDialog(getContext().getString(R.string.str_loading_wait));
    }

    public void showWaitDialog(String msg) {
        TextView msgTv = (TextView) findViewById(R.id.loading_msg);
        msgTv.setText(msg);
        if (!this.isShowing()) {
            this.show();
        }
    }

    public void dismissWaitDialog() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }

    /**
     * Des 用到的地方，都要手动释放，建议放在BaseActivity中
     * Create By better on 2016/10/20 09:50.
     */
    public static void destroyDialog(@NonNull Context context) {
        final String key = context.getClass().getSimpleName();
        if (null != listWaitDialog.get(key)) {
            listWaitDialog.get(key).dismissWaitDialog();
            listWaitDialog.remove(key);
        }
    }
}