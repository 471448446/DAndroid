package com.better.banner;

import android.support.v4.app.Fragment;

/**
 * Des 数据填充
 * Create By com.better on 2017/1/11 13:14.
 */
public interface ItemAdapter {
    Fragment getItem(int p);

    int getCount();
}
