package cn.ben.tvdemo.mainpage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;

class MainPagePagerAdapter extends FragmentPagerAdapter {
    private final SparseArrayCompat<Fragment> mFragmentsMap;

    MainPagePagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentsMap = new SparseArrayCompat<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentsMap.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentsMap.size();
    }

    void setFragment(MainPageActivity.FragmentPosition pos, Fragment fragment) {
        mFragmentsMap.put(pos.ordinal(), fragment);
    }

    Fragment getItem(MainPageActivity.FragmentPosition pos) {
        return getItem(pos.ordinal());
    }
}
