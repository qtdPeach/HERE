package com.example.user.wase.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.user.wase.view.fragment.EquipmentScanner;
import com.example.user.wase.view.fragment.ExerciseFragment;
import com.example.user.wase.view.fragment.MyRecordsFragment;

/**
 * Created by ymbae on 2016-04-18.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    Fragment deviceList;
    Fragment exercise;
    Fragment myRecord;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        deviceList = new EquipmentScanner();
        exercise = new ExerciseFragment();
        myRecord = new MyRecordsFragment();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return deviceList;
            case 1:
                return exercise;
            case 2:
                return myRecord;
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
