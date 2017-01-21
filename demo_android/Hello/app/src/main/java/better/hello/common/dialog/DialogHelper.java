package better.hello.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;

import better.hello.R;
import better.hello.common.PermissionGrantHelper;


public class DialogHelper {

    public static SimpleSubmitDialog callPhone(String msg, String negStr, OnClickListener negListener, String posStr, OnClickListener posListener) {
        return SimpleSubmitDialog.getInstance("", msg, negStr, negListener, posStr, posListener, Gravity.CENTER);
    }

    public static SimpleSubmitDialog getPermissionDeny(final Activity activity, String msg, OnClickListener negListener, final OnClickListener posListener) {
        SimpleSubmitDialog dialog = SimpleSubmitDialog.getInstance(activity.getString(R.string.str_permission_refuse_title), msg, activity.getString(R.string.str_permission_refuse_leftStr), negListener,
                activity.getString(R.string.str_permission_refuse_rightStr), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionGrantHelper.forWordGrantPermission(activity);
                        if (null != posListener) posListener.onClick(dialog, which);
                    }
                });
        dialog.setCancelable(false);
        return dialog;
    }

    public static SimpleSubmitDialog getSimpleSubmitDialogFra(String title, String msg, String negStr, OnClickListener negListener, String posStr, OnClickListener posListener) {
        return SimpleSubmitDialog.getInstance(title, msg, negStr, negListener, posStr, posListener);
    }

    public static SimpleSubmitDialog getSimpleSubmitDialogFra(String title, String msg, OnClickListener negListener, OnClickListener posListener) {
        return getSimpleSubmitDialogFra(title, msg, "", negListener, "", posListener);
    }

    public static SimpleSubmitDialog getSimpleSubmitDialogFra(Context context, int title, int msg, OnClickListener negListener, OnClickListener posListener) {
        return getSimpleSubmitDialogFra(context.getString(title), context.getString(msg), negListener, posListener);
    }

    public static SimpleSubmitDialog getSimpleSubmitDialogFra(Context context, int title, String msg, OnClickListener negListener, OnClickListener posListener) {
        return getSimpleSubmitDialogFra(context.getString(title), msg, negListener, posListener);
    }

    /**
     * Des 只有Content且只监听确认事件
     * Create By better on 16/6/29 下午4:37.
     */
    public static SimpleSubmitDialog getSimpleSubmitDialogFra(Context context, int msg, OnClickListener posListener) {
        return getSimpleSubmitDialogFra(context.getString(msg), posListener);
    }

    public static SimpleSubmitDialog getSimpleSubmitDialogFra(String msg, OnClickListener posListener) {
        return getSimpleSubmitDialogFra(null, msg, null, posListener);
    }

    public static SimpleSubmitDialog getSimpleSubmitDialogOnBtnFra(String title, String msg, OnClickListener posListener) {
        return SimpleSubmitDialog.getOneBtnDialog(title, msg, posListener);
    }

    public static SimpleSubmitDialog getSimpleSubmitDialogOnBtnFra(Context context, int title, int msg, OnClickListener posListener) {
        return getSimpleSubmitDialogOnBtnFra(context.getString(title), context.getString(msg), posListener);
    }

    public static SimpleSubmitDialog getSimpleSubmitDialogOnBtnFra(Context context, int msg, OnClickListener posListener) {
        return getSimpleSubmitDialogOnBtnFra("", context.getString(msg), posListener);
    }

    public static SimpleSubmitDialog getSimpleSubmitDialogOnBtnFra(String msg, OnClickListener posListener) {
        return getSimpleSubmitDialogOnBtnFra("", msg, posListener);
    }

    public static SimpleSubmitDialog getOneBtnNotExitDialogFra(String title, String msg, OnClickListener posListener) {
        SimpleSubmitDialog dialog = SimpleSubmitDialog.getOneBtnDialog(title, msg, posListener);
        dialog.setCancelable(false);
        return dialog;
    }
}
