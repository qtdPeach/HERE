package com.example.user.wase.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.user.wase.viewtemp.fragment.DeviceListFragment;
import com.example.user.wase.viewtemp.fragment.ExerciseFragment;
import com.example.user.wase.viewtemp.fragment.MyRecordsFragment;

/**
 * Created by ymbae on 2016-04-18.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DeviceListFragment tab1 = new DeviceListFragment();
                return tab1;
            case 1:
                ExerciseFragment tab2 = new ExerciseFragment();
                return tab2;
            case 2:
                MyRecordsFragment tab3 = new MyRecordsFragment();
                return tab3;
            default:
                return null;

        }



        //return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
