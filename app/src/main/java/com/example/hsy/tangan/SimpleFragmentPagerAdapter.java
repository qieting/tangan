package com.example.hsy.tangan;

import android.content.Context;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by hsy on 2019/2/21.
 */

public class SimpleFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private static final String[] mTitles = {"早晨", "中午", "晚上"};
    private static final String[] mTitle2s = {"饮食", "血糖", "运动"};
    boolean qq = false;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }





    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context, int i) {
        super(fm);
        this.context = context;
        qq = true;
    }
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        Log.e("1","初始化"+position);
        if(qq)
            return  PageFragment.newInstance(position + 4);
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        if(qq)
            return mTitle2s.length;
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(qq)
            return mTitle2s[position];
        return mTitles[position];
    }
}