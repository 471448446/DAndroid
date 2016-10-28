package better.hello.common.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import better.hello.R;
import better.lib.utils.ScreenUtils;
import butterknife.ButterKnife;

/**
 * 方便统一对对话框样式
 */
public abstract class BaseDialogView extends BaseDialogFragmentView {
    protected View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.dialog_base_layout, null);
        }
        View content = LayoutInflater.from(getActivity()).inflate(getContentViewLayId(), null);
        ((FrameLayout) mView.findViewById(R.id.dialog_base_frame_content)).addView(content);
        ButterKnife.bind(this, content);
        return mView;
    }

    /**
     * 获取ContentView
     */
    protected abstract int getContentViewLayId();


    @Override
    protected int[] getWH() {
        int[] wh = {(int) (ScreenUtils.getScreenWidth(getActivity()) * 0.8), getDialog().getWindow().getAttributes().height};
        return wh;
    }

    @Override
    protected boolean isNoTitle() {
        return true;
    }
}
