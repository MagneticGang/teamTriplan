package com.hanul.team1.triplan.ysh;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainPageAdapter extends FragmentStatePagerAdapter {

    public MainPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new PlanListFragment();
            case 1:
                return new Fn2Fragment();
            case 2:
                return new Fn3Fragment();
            case 3:
                return new MyPageFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "일정";
            case 1:
                return "기능2";
            case 2 :
                return "기능3";
            case 3:
                return "My";
            default:
                return null;
        }
    }
}
