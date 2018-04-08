package better.android.library.empty;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


public class EmptyViewProxy implements EmptyViewRefreshProxy, View.OnClickListener {
    private View loadingRetryView; //总体View
    private View loadingView, retryView;
    private TextView loadingViewMsg, retryViewMsg, retryButton;
    private OnLrpRetryClickListener mListener = null;
    private Context context;

    public EmptyViewProxy(Context context) {
        loadingRetryView = View.inflate(context, R.layout.empty_loading_retry, null);
        this.context = context;
        // 加载中
        loadingView = loadingRetryView.findViewById(R.id.lr_proxy_loading);
        loadingViewMsg = loadingView.findViewById(R.id.lr_proxy_loading_msg);
        // 重试
        retryView = loadingRetryView.findViewById(R.id.lr_proxy_retry);
        retryViewMsg = retryView.findViewById(R.id.lr_proxy_retry_message);
        retryButton = retryView.findViewById(R.id.lr_proxy_button_retry);
        retryButton.setOnClickListener(this);
        displayLoading();
    }

    /**
     * Default display loading:
     * You can set background, and message by the following methods
     */
    @Override
    public void displayLoading() {
        displayLoading("");
    }

    @Override
    public void displayLoading(int msgId) {
        displayCustomBkgLoading(msgId, R.color.bg);
    }

    @Override
    public void displayLoading(String msg) {
        displayCustomBkgLoading(msg, R.color.bg);
    }

    public void displayCustomBkgLoading(int msgId, int bkgId) {
        displayCustomBkgLoading(context.getString(msgId), bkgId);
    }

    public void displayCustomBkgLoading(String msg, int bkgId) {
        loadingViewMsg.setText(TextUtils.isEmpty(msg) ? "" : msg);
        loadingView.setBackgroundResource(bkgId);
        setGone(retryView);
        setVisible(loadingRetryView, loadingView);
    }

    /**
     * Display no message for T_RESULT_NO_DATA or certain msg, but not fail.
     */
    public void displayMessage(String msg) {
        displayMessage(msg, false);
    }

    public void displayMessage(int id) {
        displayMessage(id, false);
    }

    @Override
    public void displayMessage() {
        displayMessage("", false);
    }

    public void displayMessage(int id, boolean retry) {
        displayMessage(context.getString(id), retry);
    }

    public void displayMessage(String msg, boolean retry) {
        setGone(loadingView);
        setVisible(loadingRetryView, retryView);
        if (!retry) {
            setGone(retryButton);
        } else {
            setVisible(retryButton);
            retryButton.setText(R.string.str_empty_retry_btn_refresh);
        }
        retryViewMsg.setText(TextUtils.isEmpty(msg) ? "" : msg);
    }

    @Override
    public void disappear() {
        setGone(loadingRetryView);
    }

    @Override
    public void displayRetry(int msgId) {
        displayRetry(context.getString(msgId));
    }

    @Override
    public void displayRetry(String msg) {
        setVisibleGone(retryView, loadingView);
        setVisible(loadingRetryView, retryButton);
        retryViewMsg.setText(TextUtils.isEmpty(msg) ? "" : msg);
        retryButton.setText(R.string.str_empty_retry);
    }

    /**
     * Display retry message and button
     */
    @Override
    public void displayRetry() {
        displayRetry(context.getString(R.string.str_empty_retry_hint));
    }

    public void displayNone() {
        setGone(retryView, loadingView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lr_proxy_button_retry) {
            if (null != mListener) {
                mListener.onRetryClick();
            }
        }
    }

    @Override
    public View getProxyView() {
        return loadingRetryView;
    }

    @Override
    public void setOnRetryClickListener(OnLrpRetryClickListener listener) {
        mListener = listener;
    }

    private void setGone(View... views) {
        for (View view : views) {
            if (null != view) {
                view.setVisibility(View.GONE);
            }
        }
    }

    private void setVisible(View... views) {
        for (View view : views) {
            if (null != view) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setVisibleGone(View... views) {
        for (int i = 0; i < views.length; i++) {
            if (0 == i) {
                views[0].setVisibility(View.VISIBLE);
            } else {
                views[i].setVisibility(View.GONE);
            }
        }
    }

}
