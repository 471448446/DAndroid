package better.learn.showtip;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

/**
 * Created by better on 2018/1/7 14:35.
 */

public class BaseDialog extends DialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.Trans);

    }

    @Override
    public void onStart() {
        super.onStart();
        int[] wh = getWH();
        if (null != wh) {
            getDialog().getWindow().setLayout(wh[0], wh[1]);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f69603")));
//            getDialog().getWindow().setBackgroundDrawable(null);
        }
    }

    protected int[] getWH() {
        return null;
    }

}
