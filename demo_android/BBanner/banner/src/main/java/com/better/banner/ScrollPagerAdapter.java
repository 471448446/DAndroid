package com.better.banner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import static com.better.banner.BBanner.log;

/**
 * Created by com.better on 2017/1/11.
 */

public class ScrollPagerAdapter extends FragmentPagerAdapter {

    private boolean mScrollEndlessLess;
    private boolean mIsOneItemScroll;
    private int MaxNum;
    private ItemAdapter mItemAdapter;

    public ScrollPagerAdapter setMaxNum(int maxNum) {
        MaxNum = maxNum;
        notifyDataSetChanged();
        return this;
    }

    public ScrollPagerAdapter setScrollEndlessLess(boolean scrollEndlessLess) {
        mScrollEndlessLess = scrollEndlessLess;
        notifyDataSetChanged();
        return this;
    }

    public ScrollPagerAdapter setOneItemScroll(boolean oneItemScroll) {
        mIsOneItemScroll = oneItemScroll;
        notifyDataSetChanged();
        return this;
    }

    public ScrollPagerAdapter setItemAdapter(ItemAdapter itemAdapter) {
        mItemAdapter = itemAdapter;
        notifyDataSetChanged();
        return this;
    }

    public ScrollPagerAdapter(FragmentManager fm, ItemAdapter itemAdapter) {
        super(fm);
        mItemAdapter = itemAdapter;
    }

    @Override
    public Fragment getItem(int arg0) {
        if (null == mItemAdapter) return null;
        int relP;
        relP = getRelP(arg0);
        log("banner 取的位置：argo:" + arg0 + ",真实位置:" + relP);
        return null == mItemAdapter ? null : mItemAdapter.getItem(relP);
    }

    public int getRelP(int arg0) {
        if (mScrollEndlessLess) {
            int size = mItemAdapter.getCount();
            return arg0 % size;
        } else {
            return arg0;
        }
    }

    public int getLength() {
        return null == mItemAdapter ? 0 : mItemAdapter.getCount();
    }

    @Override
    public int getCount() {
        int allCount;
//        if (null == mItemAdapter || 0 == mItemAdapter.getCount()) {
//            allCount = 0;
//        } else if (1 == mItemAdapter.getCount()) {
//            if (mIsOneItemScroll) {
//                allCount = MaxNum;
//            } else {
//                allCount = 1;
//            }
//        } else {
//            allCount = MaxNum;
//        }

        if (null == mItemAdapter || 0 == mItemAdapter.getCount()) {
            allCount = 0;
        } else if (mScrollEndlessLess) {
            if (1 == mItemAdapter.getCount()) {
                if (mIsOneItemScroll) {
                    allCount = MaxNum;
                } else {
                    allCount = 1;
                }
            } else {
                allCount = MaxNum;
            }
        } else {
            allCount = mItemAdapter.getCount();
        }
        log("adapter allcount:" + allCount);
        return allCount;
    }
}
