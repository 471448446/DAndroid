package com.better.banner;

import androidx.fragment.app.Fragment;

import java.util.List;

/**
 * Des 数据填充
 * 1.构造传入数据或无参构造者重写getCount;
 * 2.重写getItem获取数据。
 * Create By com.better on 2017/1/11 13:14.
 */
public abstract class ItemAdapter {
    List<?> list;

    public ItemAdapter(List<?> list) {
        this.list = list;
    }

    public ItemAdapter() {

    }

    /**
     * Des 对应位置的页面
     * Create By better on 2017/2/15 14:49.
     */
    public abstract Fragment getItem(int p);

    /**
     * Des banner的实际页面个数
     * Create By better on 2017/2/15 14:49.
     */
    public int getCount() {
        return null == list ? 0 : list.size();
    }
}
