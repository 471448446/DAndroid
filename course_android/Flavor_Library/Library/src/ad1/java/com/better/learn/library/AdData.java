package com.better.learn.library;

import android.content.Context;
import android.widget.Toast;

public class AdData extends BaseAdData {
    @Override
    public void showAd(Context context) {
        Toast.makeText(context, "ad Show", Toast.LENGTH_SHORT).show();
    }
}
