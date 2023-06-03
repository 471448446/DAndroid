package better.learn.showtip;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by better on 2018/1/7 16:37.
 */

public class TipDialog extends BaseDialog {
    private OnGetBitShowListener mOnGetBitShowListener;
    private TipView mTipView;

    public TipDialog setOnGetBitShowListener(OnGetBitShowListener onGetBitShowListener) {
        mOnGetBitShowListener = onGetBitShowListener;
        return this;
    }

    public interface OnGetBitShowListener {
        TipBean getBitmap();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_tip, container, false);
        mTipView = (TipView) view.findViewById(R.id.tip_tip);
        view.findViewById(R.id.tip_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null != mOnGetBitShowListener.getBitmap()) {
            mTipView.setTipBean(mOnGetBitShowListener.getBitmap());
        }
    }

    @Override
    protected int[] getWH() {
        return new int[]{ScreenUtilsPlus.getScreenWidth(), ScreenUtilsPlus.getScreenHeight()};
    }
}
