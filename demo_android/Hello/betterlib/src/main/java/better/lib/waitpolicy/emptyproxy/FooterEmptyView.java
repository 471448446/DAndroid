package better.lib.waitpolicy.emptyproxy;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import better.lib.R;
import better.lib.utils.BaseUtils;

/**
 * Created by Better on 2016/3/13.
 */
public class FooterEmptyView extends EmptyViewProxy implements View.OnClickListener {

    private RelativeLayout loadingRetryView; //总体View
    private onLrRetryClickListener mListener = null;
    private View loadingView, retryView;
    private TextView loadingViewMsg;
    private TextView retryViewMsg;
    private Button retryButton;
    private Context context;

    public FooterEmptyView(Context context) {
        loadingRetryView = (RelativeLayout) View.inflate(context, R.layout.footer_loading_retry, null);
        this.context = context;
        // 加载中
        loadingView = loadingRetryView.findViewById(R.id.footer_proxy_loading);
        loadingViewMsg = (TextView) loadingView.findViewById(R.id.footer_proxy_loading_msg);
        // 重试
        retryView = loadingRetryView.findViewById(R.id.footer_proxy_retry);
        retryViewMsg = (TextView) retryView.findViewById(R.id.footer_proxy_retry_message);
        retryButton = (Button) retryView.findViewById(R.id.footer_proxy_button_retry);
        retryButton.setOnClickListener(this);
        displayLoading();
    }

    @Override
    public View getProxyView() {
        return loadingRetryView;
    }

    /**
     * Default display loading:
     * You can set background, and message by the following methods
     */
    @Override
    public void displayLoading() {
        BaseUtils.setVisibleGone(loadingView, retryView);
    }
    @Override
    public void displayLoading(String msg) {
        displayCustomBkgLoading(msg, android.R.color.white);
    }

    public void displayLoading(int msgId) {
        displayCustomBkgLoading(msgId, android.R.color.white);
    }

    public void displayCustomBkgLoading(int msgId, int bkgId) {
        loadingViewMsg.setText(msgId);
        loadingView.setBackgroundResource(bkgId);
        BaseUtils.setVisibleGone(loadingView, retryView);
    }

    public void displayCustomBkgLoading(String msg, int bkgId) {
        loadingViewMsg.setText(msg);
        loadingView.setBackgroundResource(bkgId);
        BaseUtils.setVisibleGone(loadingView, retryView);
    }

    @Override
    public void displayMessage() {
        displayMessage(R.string.str_loading_wait);
    }

    /**
     * Display no message for T_RESULT_NO_DATA or certain msg, but not fail.
     *
     */
    @Override
    public void displayMessage(String msg) {
        displayMessage(msg, false);
    }

    public void displayMessage(int id) {
        displayMessage(id, false);
    }

    public void displayMessage(String msg, boolean retry) {
        BaseUtils.setVisibleGone(retryView, loadingView);
        if (!retry) {
            BaseUtils.setGone(retryButton);
        } else {
            BaseUtils.setVisible(retryButton);
            retryButton.setText(R.string.str_loading_refresh);
        }
        retryViewMsg.setText(msg);
    }

    public void displayMessage(int id, boolean retry) {
        displayMessage(context.getString(id), retry);
    }


    /**
     * Display retry message and button
     */
    @Override
    public void displayRetry() {
        displayRetry(context.getString(R.string.str_loading_retry_hint));
    }
    @Override
    public void displayRetry(String msg) {
        BaseUtils.setVisibleGone(retryView, loadingView);
        retryViewMsg.setText(msg);
    }

    public void displayRetry(int msgId) {
        displayRetry(context.getString(msgId));
    }

    @Override
    public void displayNone() {
        BaseUtils.setGone(retryView, loadingView);
    }

//	public interface onLrRetryClickListener {
//		public abstract void onRetryClick();
//	}

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lr_proxy_button_retry) {
            if (null != mListener) {
                mListener.onRetryClick();
            }
        }
    }
}
