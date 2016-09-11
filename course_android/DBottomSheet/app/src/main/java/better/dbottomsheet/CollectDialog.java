package better.dbottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by better on 16/8/31.
 */
public class CollectDialog extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.diaog_collect,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Better","re="+getResources().getDisplayMetrics().widthPixels);
        Log.d("Better","de="+getMetrics(getActivity()).widthPixels);
        getDialog().getWindow().setLayout(getResources().getDisplayMetrics().widthPixels,getDialog().getWindow().getAttributes().height);
    }
    public DisplayMetrics getMetrics(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }
}
