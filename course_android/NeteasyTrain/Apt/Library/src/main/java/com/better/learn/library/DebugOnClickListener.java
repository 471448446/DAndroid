package com.better.learn.library;

import android.view.View;

/**
 * Created by better on 2019-05-19 10:49.
 */
public abstract class DebugOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        doClick(v);
    }

    public abstract void doClick(View view);
}
