package better.learn.showtip;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 背景图框住指定大小的透明范围
 * Created by better on 2018/1/7 14:38.
 */

public class OneTipDialog extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.dialog_tip_one, container, false);
    }

//    @Override
//    protected int[] getWH() {
//        return new int[]{ScreenUtilsPlus.getScreenWidth(), ScreenUtilsPlus.getScreenHeight()};
//    }
}
