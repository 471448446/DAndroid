package com.better.banner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import static com.better.banner.BBanner.log;

/**
 * Created by com.better on 2017/1/11.
 */

public class ScrollPagerAdapter extends FragmentPagerAdapter {

    private boolean mIsOneItemScroll;
    private int MaxNum;
    private ItemAdapter mItemAdapter;

    public void setMaxNum(int maxNum) {
        MaxNum = maxNum;
        notifyDataSetChanged();
    }

    public void setOneItemScroll(boolean oneItemScroll) {
        mIsOneItemScroll = oneItemScroll;
        notifyDataSetChanged();
    }

    public void setItemAdapter(ItemAdapter itemAdapter) {
        mItemAdapter = itemAdapter;
        notifyDataSetChanged();
    }

    public ScrollPagerAdapter(FragmentManager fm, ItemAdapter itemAdapter) {
        super(fm);
        mItemAdapter = itemAdapter;
    }

    @Override
    public Fragment getItem(int arg0) {
        if (null == mItemAdapter) return null;
        int size = mItemAdapter.getCount();
        int relP = arg0 % size;
        log("banner 取的位置：argo:" + arg0 + ",真实位置:" + relP);
        return null == mItemAdapter ? null : mItemAdapter.getItem(relP);
    }

    public int getLength() {
        return null == mItemAdapter ? 0 : mItemAdapter.getCount();
    }

    @Override
    public int getCount() {
        int allCount;
        if (null == mItemAdapter || 0 == mItemAdapter.getCount()) {
            allCount = 0;
        } else if (1 == mItemAdapter.getCount()) {
            if (mIsOneItemScroll) {
                allCount = MaxNum;
            } else {
                allCount = 1;
            }
        } else {
            allCount = MaxNum;
        }
        log("adapter allcount:" + allCount);
        return allCount;
    }
}
