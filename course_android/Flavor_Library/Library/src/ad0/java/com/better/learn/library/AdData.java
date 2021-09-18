package com.better.learn.library;

import android.content.Context;
import android.widget.Toast;

/**
 * no ad implementation
 */
public class AdData extends BaseAdData {
    @Override
   public void showAd(Context context) {
        Toast.makeText(context, "this channel no ad", Toast.LENGTH_SHORT).show();
    }
}
