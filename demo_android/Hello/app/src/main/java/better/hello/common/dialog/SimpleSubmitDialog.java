package better.hello.common.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import better.hello.R;
import better.hello.util.Utils;

/**
 * Des 提交对话框<br>
 * 没有标题时---content文字居中,上下间距加宽
 * 底部只有一个btn->getOneBtnDialog();
 * 底部两个btn->getInstance();
 * Created by Better on 2016/5/19 14:07.
 */
public class SimpleSubmitDialog extends BaseDialogView {
    TextView mTvTitle;
    TextView mTvContent;
    Button negBtn, posBtn;
    public Dialog.OnClickListener cancelListener, confirmListener;

    public static SimpleSubmitDialog getInstance(String title, String msg, Dialog.OnClickListener cancelListener, Dialog.OnClickListener confirmListener) {
        return getInstance(title, msg, "", cancelListener, "", confirmListener);
    }

    public static SimpleSubmitDialog getInstance(String title, String msg, String negStr, Dialog.OnClickListener cancelListener, String posStr, Dialog.OnClickListener confirmListener) {
        return getInstance(title, msg, negStr, cancelListener, posStr, confirmListener, Gravity.LEFT);
    }

    public static SimpleSubmitDialog getInstance(String title, String msg, String negStr, Dialog.OnClickListener cancelListener, String posStr,
                                                 Dialog.OnClickListener confirmListener, int gravity) {
        return getInstance(title, msg, negStr, cancelListener, posStr, confirmListener, gravity, false);
    }

    public static SimpleSubmitDialog getInstance(String title, String msg, String negStr, Dialog.OnClickListener cancelListener, String posStr,
                                                 Dialog.OnClickListener confirmListener, int gravity, boolean isOnBtn) {
        SimpleSubmitDialog d = new SimpleSubmitDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Intent.EXTRA_TITLE, title);
        bundle.putString(Intent.EXTRA_TEXT, msg);
        bundle.putString(Intent.EXTRA_ALARM_COUNT, negStr);
        bundle.putString(Intent.EXTRA_KEY_EVENT, posStr);
        bundle.putInt(Intent.ACTION_ATTACH_DATA, gravity);
        bundle.putBoolean(Intent.ACTION_ANSWER, isOnBtn);
        d.setArguments(bundle);
        d.cancelListener = cancelListener;
        d.confirmListener = confirmListener;
        return d;
    }

    public static SimpleSubmitDialog getOneBtnDialog(String title, String msg, Dialog.OnClickListener confirmListener) {
        return getOneBtnDialog(title, msg, "", confirmListener);
    }

    public static SimpleSubmitDialog getOneBtnDialog(String title, String msg, String posStr, Dialog.OnClickListener confirmListener) {
        return getOneBtnDialog(title, Gravity.LEFT, msg, "", confirmListener);
    }

    public static SimpleSubmitDialog getOneBtnDialog(String title, int gravity, String msg, String posStr, Dialog.OnClickListener confirmListener) {
        return getInstance(title, msg, "", null, posStr, confirmListener, gravity, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        negBtn = (Button) mView.findViewById(R.id.dialog_simple_btn_cancel);
        posBtn = (Button) mView.findViewById(R.id.dialog_simple_btn_confirm);
        mTvTitle = (TextView) mView.findViewById(R.id.dialog_simple_tv_title);
        mTvContent = (TextView) mView.findViewById(R.id.dialog_simple_submit_content);
        negBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (null != cancelListener){
                    cancelListener.onClick(null, 0);
                }
            }
        });
        posBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (null != confirmListener) {
                    confirmListener.onClick(null, 0);
                }
            }
        });

        Bundle b = getArguments();
        if (null != b) {
            if (0 != b.getInt(Intent.ACTION_ATTACH_DATA)) {
                mTvContent.setGravity(b.getInt(Intent.ACTION_ATTACH_DATA));
            }
            if (TextUtils.isEmpty(b.getString(Intent.EXTRA_TITLE))) {
                Utils.setGone(mTvTitle);
                //没有标题的时候好看一点 用于乘车异常dialog
                mTvContent.setPadding(30, 60, 30, 60);
            } else {
                mTvTitle.setText(b.getString(Intent.EXTRA_TITLE));
            }
            if (TextUtils.isEmpty(b.getString(Intent.EXTRA_TEXT))) {
                mTvContent.setText("");
            } else {
                mTvContent.setText(b.getString(Intent.EXTRA_TEXT));
            }
            if (!TextUtils.isEmpty(b.getString(Intent.EXTRA_ALARM_COUNT))) {
                negBtn.setText(b.getString(Intent.EXTRA_ALARM_COUNT));
            }
            if (!TextUtils.isEmpty(b.getString(Intent.EXTRA_KEY_EVENT))) {
                posBtn.setText(b.getString(Intent.EXTRA_KEY_EVENT));
            }
            //页面底部只有一个按钮
            if (b.getBoolean(Intent.ACTION_ANSWER)) {
                Utils.setGone(negBtn);
                posBtn.setBackgroundResource(R.drawable.shape_dialog__btn_one);
                if (!TextUtils.isEmpty(b.getString(Intent.EXTRA_KEY_EVENT))) {
                    posBtn.setText(b.getString(Intent.EXTRA_KEY_EVENT));
                }
            }
        }
    }


    @Override
    protected int getContentViewLayId() {
        return R.layout.dialog_simple_submt;
    }
}
