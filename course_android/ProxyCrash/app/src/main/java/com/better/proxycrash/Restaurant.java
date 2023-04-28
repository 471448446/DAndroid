package com.better.proxycrash;

import android.content.Context;
import android.util.Log;

public class Restaurant implements Cooker.ScheduleListener {

    @Override
    public void onDone() {
        Log.e("Better", "food has cooked!!!");
    }

    public void order(Context context) {
        Cooker cooker = new Cooker();
        cooker.setListener(this);
        cooker.cook();
    }
}
